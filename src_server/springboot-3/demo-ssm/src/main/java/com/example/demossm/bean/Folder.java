package com.example.demossm.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/10/22 09:37
 */

@Component
@Data
public class Folder {
    private Integer id;
    private String name;
    private Integer userId;
}
