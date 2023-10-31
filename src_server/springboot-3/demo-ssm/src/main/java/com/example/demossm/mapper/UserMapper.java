package com.example.demossm.mapper;


import com.example.demossm.bean.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper{
    User getUserById(@Param("id") Long id);
    User getUserByUserName(@Param("username") String username);

    void insertUser(@Param("username") String username,@Param("password") String password);
}
