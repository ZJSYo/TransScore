package com.example.demossm.mapper;

import com.example.demossm.bean.Folder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 09:44
 */

public interface FolderMapper {
    List<Folder> getFolderListByUserId(Integer userId);

    void insertFolder(@Param("folderName") String folderName, @Param("userId") Integer userId);

    void deleteFolder(@Param("folderId") Integer folderId);
}
