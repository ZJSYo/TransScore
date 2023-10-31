package com.example.demossm.controller;

import com.example.demossm.bean.Folder;
import com.example.demossm.response.FolderResponse;
import com.example.demossm.service.FileService;
import com.example.demossm.service.FolderSevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 09:48
 */
@RestController
@Slf4j
public class FolderController {
    @Autowired
    FolderSevice folderSevice;
    @Autowired
    FileService fileService;
    ObjectMapper mapper = new ObjectMapper();
    @GetMapping("/folder")
    public FolderResponse getFolderListByUserId(@RequestParam("userid")Integer userId){
        log.info("get folderlist by userid"+userId);
        List<Folder> folders = folderSevice.getFolderListByUserId(userId);
        //返回json数据，添加status字段
        FolderResponse folderResponse = new FolderResponse("ok",folders);
        return folderResponse;
    }
    @PostMapping("/folder")
    public String insertFolder(@RequestBody Folder folder){
        String folderName = folder.getName();
        Integer userId = folder.getUserId();
        log.info("insert folder");
        String res = "";
        try{
            folderSevice.insertFolder(folderName,userId);
            FolderResponse folderResponse = new FolderResponse("ok",null);
            res = mapper.writeValueAsString(folderResponse);
        }catch (Exception e){
            FolderResponse folderResponse = new FolderResponse("error",null);
            try{
                res = mapper.writeValueAsString(folderResponse);
            }catch (Exception e1){
                res = "{status"+":"+"error";
            }
        }finally {
            return res;
        }
    }
    @DeleteMapping("/folder")
    public String deleteFolder(@RequestParam("folderid") Integer folderId){
        log.info("delete folder");
        String res = "";
        try{
            folderSevice.deleteFolder(folderId);
            fileService.deleteFileByFolderId(folderId);
            FolderResponse folderResponse = new FolderResponse("ok",null);
            res = mapper.writeValueAsString(folderResponse);
        }catch (Exception e){
            FolderResponse folderResponse = new FolderResponse("error",null);
            try{
                res = mapper.writeValueAsString(folderResponse);
            }catch (Exception e1){
                res = "{status"+":"+"error";
            }
        }finally {
            return res;
        }
    }
}
