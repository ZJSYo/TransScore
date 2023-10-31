package com.example.demossm.controller;

import com.example.demossm.bean.User;
import com.example.demossm.mapper.UserMapper;
import com.example.demossm.response.UserResponse;
import com.example.demossm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    ObjectMapper mapper = new ObjectMapper();
    @GetMapping("/user/{id}")
    public String  getUserById(@PathVariable("id") Long id){
        log.info("get user by id");
        return userService.getUserById(id).toString();
    }
    @GetMapping("/user")
    public UserResponse getUserByUserName(@RequestParam("username") String username){
        log.info("get user by username");
        List<User> users = new ArrayList<User>();
        if(userService.getUserByUserName(username)!=null)
            users.add(userService.getUserByUserName(username));
        return new UserResponse("ok",users);
    }
    @PostMapping("/user")
    public String insertUser(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        log.info("insert User");
        try{
            userService.insertUser(username,password);
            List<User> users = new ArrayList<User>();
            users.add(userService.getUserByUserName(username));
            System.out.println("OK");

            UserResponse userResponse =  new UserResponse("ok",users);
            //将其转换为json格式

            String jsonString = mapper.writeValueAsString(userResponse);
            return jsonString;

        }catch (Exception e){
            System.out.println("Error");
//            return new String("error");

            UserResponse userResponse = new UserResponse("error",null);
            try{
                String jsonString = mapper.writeValueAsString(userResponse);
                return jsonString;
            }catch (Exception e1){
                System.out.println("Error");
                return new String("error");
            }
        }
    }
}
