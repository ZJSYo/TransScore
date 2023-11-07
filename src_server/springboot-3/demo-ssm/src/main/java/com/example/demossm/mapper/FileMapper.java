package com.example.demossm.mapper;

import com.example.demossm.bean.MediaFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 10:58
 */

public interface FileMapper {
    List<MediaFile> getFileListByFolderId(Integer folderId);

    void insertFile(@Param("fileName") String fileName, @Param("folderId") Integer folderId, @Param("filePath") String filePath, @Param("type") Integer type);

    void deleteFile(Integer fileId);
    void deleteFileByFolderId(Integer folderId);
    MediaFile getFileByFileId(@Param("id") Integer fileId);

}
