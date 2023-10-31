package com.example.demossm.response;

import com.example.demossm.bean.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 10:09
 */
@Data
@Component
public class UserResponse {
    private String status;
    private List<User> data;
    public UserResponse(){

    }
    public UserResponse(String status,List<User> data){
        this.status=status;
        this.data=data;
    }
}
