package com.example.demossm.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/8/15 10:52
 */
@Component
@Data
public class User {
    private Long id;
    private String username;
    private String password;
}
