package com.twigle.spike.controller;

import com.twigle.spike.domain.SpikeUser;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.result.Result;
import com.twigle.spike.service.SpikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    SpikeService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<SpikeUser> info(Model model, SpikeUser user) {
        return Result.success(user);
    }

}
