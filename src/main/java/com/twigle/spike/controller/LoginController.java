package com.twigle.spike.controller;

import com.twigle.spike.result.Result;
import com.twigle.spike.service.SpikeUserService;
import com.twigle.spike.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {
    @Autowired
    SpikeUserService spikeUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }


    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {

        log.info(loginVo.toString());

        spikeUserService.login(response, loginVo);
        return Result.success(true);
    }
//generate token for Jmeter
//    @RequestMapping("/do_login")
//    @ResponseBody
//    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
//
//        log.info(loginVo.toString());
//
//        String token = spikeUserService.login(response, loginVo);
//        return Result.success(token);
//    }
}
