package com.example.demossm.service;

import com.example.demossm.bean.User;
import com.example.demossm.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangjinshen
 * @description TODO
 * @date 2023/8/15 11:19
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public User getUserById(Long id){
        return userMapper.getUserById(id);
    }
    public User getUserByUserName(String username){
        return userMapper.getUserByUserName(username);
    }
    public void insertUser(String username,String password){
        userMapper.insertUser(username,password);
    }
}
