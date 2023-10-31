package com.example.demossm.response;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/26 15:19
 */
@Data
@Component
public class MediaResponse {
    private String status;
    private String data;
    public MediaResponse(){

    }
    public MediaResponse(String status, String data){
        this.status=status;
        this.data=data;
    }

}