package com.example.demossm.controller;

import com.example.demossm.bean.MediaFile;
import com.example.demossm.request.FileRequest;
import com.example.demossm.response.FileResponse;
import com.example.demossm.response.MediaResponse;
import com.example.demossm.response.UserResponse;
import com.example.demossm.service.FileService;
import com.example.demossm.utils.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 10:59
 */
@RestController
@Slf4j
public class FileController {
    @Autowired
    FileService fileService;
    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/file")
    public FileResponse getFileListByFolderId(HttpServletRequest request) {
        log.info("get Files By folderId");
        String folderid_string = "";
        try {
            folderid_string = request.getParameter("folderid");
            if (folderid_string == null)
                log.info("NULL ptr");
        } catch (Exception e) {
            log.info("folder not exist");
        }

        Integer folderId = Integer.valueOf(folderid_string);
        //log--记录日志
        log.info("folderid:" + folderId);
        List<MediaFile> mediaFiles = fileService.getFileListByFolderId(folderId);
        FileResponse ok = new FileResponse("ok", mediaFiles);
        log.info(ok.toString());
        return ok;
    }

    //
    @PostMapping("/file")
    public String addFile(@RequestBody FileRequest request) throws Exception {
        log.info("addFile");
        String res = "ok";
        String data = request.getData();
        Integer type = request.getType();
        Integer folderId = request.getFolderId();
        String name = request.getName();
        String filePath = switch (type) {
            case 1-> FileUtils.getTempName(FileUtils.AUDIO_FOLDER_PATH, FileUtils.FileType.AUDIO);
            case 2 -> FileUtils.getTempName(FileUtils.SHEET_FOLDER_PATH, FileUtils.FileType.IMAGE);
            default -> "";
        };
        byte[] bytes = FileUtils.decodeBase64(data);
        FileUtils.saveFile(bytes,filePath);
        try {
            //TODO 将文件信息插入数据库
            fileService.insertFile(name,folderId, filePath, type);
            res = mapper.writeValueAsString(new UserResponse("ok", null));
        } catch (Exception e) {
            try {
                res = mapper.writeValueAsString(new UserResponse("error", null));
            } catch (Exception e1) {
                res = "error";
            }
        }
        return res;
    }

    @DeleteMapping("/file")
    public String deleteFile(@RequestParam("fileid") Integer fileId) {
        String res = "ok";
        try {
            fileService.deleteFile(fileId);
            res = mapper.writeValueAsString(new UserResponse("ok", null));
        } catch (Exception e) {
            try {
                res = mapper.writeValueAsString(new UserResponse("error", null));
            } catch (Exception e1) {
                res = "error";
            }
        }
        return res;
    }
    @GetMapping("/file/download")
    public String getFileByFileId(@RequestParam("fileId") Integer fileId){
        String res = "ok";
        try {
            MediaFile fileByFileId = fileService.getFileByFileId(fileId);
            String data = FileUtils.getBaseImg(fileByFileId.getFilePath());
            res = mapper.writeValueAsString(new MediaResponse("ok",data));
        } catch (Exception e) {
            try {
                res = mapper.writeValueAsString(new UserResponse("error", null));
            } catch (Exception e1) {
                res = "error";
            }
        }
        return res;
    }

}