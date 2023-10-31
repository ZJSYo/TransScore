package com.example.demossm.controller;

import ch.qos.logback.core.util.FileUtil;
import com.example.demossm.response.MediaResponse;
import com.example.demossm.utils.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
public class TransferController {
    // 文件夹路径


    // py转换脚本路径
    private final String AUDIO2SHEET_SCRIPT_PATH = "/home/zhang/星辰大海/piano_transcription/mid2gram.py";
    private final String TEXT2AUDIO_SCRIPT_PATH = "/home/zhang/星辰大海/PIANO_TRANSCRIPTION/text2mp3.py";
    private final String IMAGE2TEXT_SCRIPT_PATH = "/home/zhang/星辰大海/CONZIC/image2text.py";
    private final String CONDA_PATH = "/home/zhang/anaconda3/envs/audioldm/bin/python";

    @PostMapping("/transfer2Sheet")
    //post请求有一个@Part MultipartFile file参数
    public String transfer2Sheet(@RequestBody String audio_data) throws Exception {
        log.info("transfer2Sheet");
        String res;
        try {
            //将传入的音频转化为文件保存
            byte[] dataBytes = FileUtils.decodeBase64(audio_data);
            String audioFilePath = FileUtils.getTempName(FileUtils.AUDIO_FOLDER_PATH, FileUtils.FileType.AUDIO);
            FileOutputStream fos = new FileOutputStream(audioFilePath);
            fos.write(dataBytes);

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(CONDA_PATH, AUDIO2SHEET_SCRIPT_PATH, audioFilePath);

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());
            log.debug(results.toString());
            String sheetPath = FileUtils.SHEET_FOLDER_PATH + FileUtils.getFileName(audioFilePath).replace(".mp3", "png");
            log.info("sheetPath:{}", sheetPath);
            String sheetBase64 = FileUtils.getBaseImg(sheetPath);
            res = FileUtils.mapper.writeValueAsString(new MediaResponse("ok", sheetBase64));
            return res;
        } catch (IOException e) {
            res = "{\"status\":\"error\"}";
            return res;
        }
    }

    @PostMapping("/transfer2Audio")
    public String transfer2Audio(@RequestBody String text) {
        log.info("transfer2Audio");
        //TODO 将text转换为audio
        //现将text保存为文件
        String textFilePath = FileUtils.getTempName(FileUtils.TEXT_FOLDER_PATH, FileUtils.FileType.TEXT);
        FileUtils.saveText(text, textFilePath);
        String res;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(CONDA_PATH, TEXT2AUDIO_SCRIPT_PATH, textFilePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            String audioPath = FileUtils.AUDIO_FOLDER_PATH + "/" + FileUtils.getFileName(textFilePath).replace(".txt", ".mp3");
            String audioBase64 = FileUtils.getBaseImg(audioPath);
            res = FileUtils.mapper.writeValueAsString(new MediaResponse("ok", audioBase64));
            return res;
        } catch (IOException e) {
            res = "{\"status\":\"error\"}";
            return res;
        }
    }

    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }
}