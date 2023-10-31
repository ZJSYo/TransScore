package com.example.demossm.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @description TODO
 * @author zhangjinshen
 * @date 2023/10/22 09:37
 */
@Data
@Component
public class MediaFile {
    private Integer id;
    private String name;
    private Integer folderId;
    private Integer type;
    private String filePath;
}
