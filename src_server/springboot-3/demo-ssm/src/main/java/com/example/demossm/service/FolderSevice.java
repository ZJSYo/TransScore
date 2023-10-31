package com.example.demossm.service;

import com.example.demossm.bean.Folder;
import com.example.demossm.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 09:47
 */
@Service
public class FolderSevice {
    @Autowired
    FolderMapper folderMapper;
    public List<Folder> getFolderListByUserId(Integer userId){
        return folderMapper.getFolderListByUserId(userId);
    }
    public void insertFolder(String folderName,Integer userId){
        folderMapper.insertFolder(folderName,userId);
    }
    public void deleteFolder(Integer folderId){
        folderMapper.deleteFolder(folderId);
    }
}
