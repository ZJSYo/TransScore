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
    private final String TEXT2AUDIO_SCRIPT_PATH = "/home/zhang/星辰大海/tango_for_se/tango/text2music.py";
    private final String IMAGE2TEXT_SCRIPT_PATH = "/home/zhang/星辰大海/ConZIC/image2text.py";
    private final String CONDA_PATH = "/home/zhang/anaconda3/envs/audioldm/bin/python";
    private List<String> audios = FileUtils.getFileNameList(FileUtils.AUDIO_FOLDER_PATH);

    @PostMapping("/transfer2Sheet")
    public String transfer2Sheet(@RequestBody String data)  {
        log.info("transfer2Sheet");
        String res;
        data = data.substring(1, data.length() - 1);
        try {
            //将传入的音频转化为文件保存
            byte[] dataBytes = FileUtils.decodeBase64(data);
            String audioFilePath = FileUtils.getTempName(FileUtils.AUDIO_FOLDER_PATH, FileUtils.FileType.AUDIO);
            FileOutputStream fos = new FileOutputStream(audioFilePath);
            fos.write(dataBytes);
            String sudoCommand = "sudo -u zhang " + CONDA_PATH + " " + AUDIO2SHEET_SCRIPT_PATH + " " + audioFilePath;
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", sudoCommand);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());
            log.info(results.toString());
            String sheetPath = FileUtils.SHEET_FOLDER_PATH + FileUtils.getFileName(audioFilePath).replace(".mp3", ".png");
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
        text = text.substring(1, text.length() - 1);
        //现将text保存为文件
        String textFilePath = FileUtils.getTempName(FileUtils.TEXT_FOLDER_PATH, FileUtils.FileType.TEXT);
        FileUtils.saveText(text, textFilePath);
        String res;
        try {
            String sudoCommand =  CONDA_PATH + " " + TEXT2AUDIO_SCRIPT_PATH + " " + textFilePath;
            File directory = new File("/home/zhang/星辰大海/tango_for_se/tango");
            ProcessBuilder processBuilder = new ProcessBuilder().directory(directory);
            processBuilder.command("bash", "-c", sudoCommand);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());
            log.info(results.toString());
            String audioPath = FileUtils.AUDIO_FOLDER_PATH + FileUtils.getFileName(textFilePath).replace(".txt", ".mp3");
            String audioBase64 = FileUtils.getBaseImg(audioPath);
            res = FileUtils.mapper.writeValueAsString(new MediaResponse("ok", audioBase64));
            return res;
        } catch (IOException e) {
            res = "{\"status\":\"error\"}";
            return res;
        }

    }
    @PostMapping("/transfer2Text")
    public String transfer2Text(@RequestBody String imgData){
        log.info("transfer2Text");
        imgData = imgData.substring(1, imgData.length() - 1);
        //将传入的图片转化为文件保存
        byte[] dataBytes = FileUtils.decodeBase64(imgData);
        String imgFilePath = FileUtils.getTempName(FileUtils.SHEET_FOLDER_PATH, FileUtils.FileType.IMAGE);
        FileUtils.saveFile(dataBytes, imgFilePath);
        String res;
        String sudoCommand = "sudo -u zhang " + CONDA_PATH + " " + IMAGE2TEXT_SCRIPT_PATH + " " + imgFilePath;
        try {
            File directory = new File("/home/zhang/星辰大海/ConZIC");
            ProcessBuilder processBuilder = new ProcessBuilder().directory(directory);
            processBuilder.command("bash", "-c", sudoCommand);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());
            log.info(results.toString());
            String textPath = FileUtils.TEXT_FOLDER_PATH  + FileUtils.getFileName(imgFilePath).replace(".png", ".txt");
            String text = FileUtils.readTxtFile(textPath);
            res = FileUtils.mapper.writeValueAsString(new MediaResponse("ok", text));
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