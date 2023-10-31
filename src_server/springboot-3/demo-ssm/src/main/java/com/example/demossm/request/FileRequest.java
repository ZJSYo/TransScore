package com.example.demossm.request;

import com.example.demossm.bean.MediaFile;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class FileRequest {
    private String name;
    private Integer folderId;
    private Integer type;
    private String data;
}

