package com.example.demossm.response;

import com.example.demossm.bean.Folder;
import com.example.demossm.service.FolderSevice;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 10:03
 */
@Data
@Component
public class FolderResponse {
    private String status;
    private List<Folder> data;
    public FolderResponse(){

    }
    public FolderResponse(String status,List<Folder> data){
        this.status=status;
        this.data=data;
    }
}
