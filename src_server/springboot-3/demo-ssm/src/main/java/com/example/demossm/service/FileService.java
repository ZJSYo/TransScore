package com.example.demossm.service;

import com.example.demossm.bean.MediaFile;
import com.example.demossm.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 10:59
 */
@Service
public class FileService {
    @Autowired
    FileMapper fileMapper;
    public void deleteFile(Integer fileId){
        fileMapper.deleteFile(fileId);
    }
    public void insertFile(String fileName,Integer folderId,String filePath,Integer type){
        fileMapper.insertFile(fileName,folderId,filePath,type);
    }
    public void deleteFileByFolderId(Integer folderId){
        fileMapper.deleteFileByFolderId(folderId);
    }
    public List<MediaFile> getFileListByFolderId(Integer folderId){
        return fileMapper.getFileListByFolderId(folderId);
    }
}
