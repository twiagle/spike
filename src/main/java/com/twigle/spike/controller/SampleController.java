package com.twigle.spike.controller;

import com.twigle.spike.model.User;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.redis.UserKey;
import com.twigle.spike.result.CodeMsg;
import com.twigle.spike.result.Result;
import com.twigle.spike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
//        public String hello() {
        return Result.success("ss");
//        System.out.println(Result.success("ss"));
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(2);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }
}
