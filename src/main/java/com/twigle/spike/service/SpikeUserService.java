package com.twigle.spike.service;

import com.twigle.spike.dao.SpikeUserDao;
import com.twigle.spike.exception.GlobalException;
import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.redis.RedisService;
import com.twigle.spike.redis.SpikeUserPrefix;
import com.twigle.spike.result.CodeMsg;
import com.twigle.spike.util.MD5Util;
import com.twigle.spike.util.UUIDUtil;
import com.twigle.spike.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
@Service
public class SpikeUserService {
    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    SpikeUserDao spikeUserDao;
    @Autowired
    RedisService redisService;
    public SpikeUser getByID(long id) {
        return spikeUserDao.getByID(id);
    }

    public SpikeUser getByToken(HttpServletResponse response,  String token) {
        if (StringUtils.isEmpty(token))
            return null;

        SpikeUser spikeUser = redisService.get(SpikeUserPrefix.Prefix, token, SpikeUser.class);
        if (spikeUser != null)
            reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.Prefix.expireSeconds());
        return spikeUser;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (null == loginVo) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        SpikeUser spikeUser = getByID(Long.parseLong(loginVo.getPhoneNumber()));
        if (null == spikeUser) throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        String calculatedPassword = MD5Util.formPassToDBPass(loginVo.getPassword(), spikeUser.getSalt());
        if (!spikeUser.getPassword().equals(calculatedPassword)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        redisService.set(SpikeUserPrefix.Prefix, token, spikeUser);
        reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.Prefix.expireSeconds());
        return token;
    }

    private void reviseCookie(HttpServletResponse response, String key, String value, int expiredSeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}



























