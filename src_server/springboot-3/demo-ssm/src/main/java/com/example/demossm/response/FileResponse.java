package com.example.demossm.response;

import com.example.demossm.bean.MediaFile;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 11:00
 */
@Component
@Data
public class FileResponse {
    private String status;
    private List<MediaFile> data;
    public FileResponse(){}
    public FileResponse(String status,List<MediaFile> data){
        this.status=status;
        this.data=data;
    }
}
