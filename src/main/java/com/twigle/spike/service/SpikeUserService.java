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

    public SpikeUser getById(long id) {
        SpikeUser user = redisService.get(SpikeUserPrefix.getById, "" + id, SpikeUser.class);
        if (user != null) return user;
        user = spikeUserDao.getByID(id);
        if (user != null) redisService.set(SpikeUserPrefix.getById, "" + id, user);
        return user;
    }

    public SpikeUser getByToken(HttpServletResponse response,  String token) {
        if (StringUtils.isEmpty(token))
            return null;

        SpikeUser spikeUser = redisService.get(SpikeUserPrefix.token, token, SpikeUser.class);
        if (spikeUser != null)
            reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.token.expireSeconds());//update cookie expire
        return spikeUser;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {

        SpikeUser spikeUser = getById(Long.parseLong(loginVo.getPhoneNumber()));
        if (null == spikeUser) throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        String calculatedPassword = MD5Util.formPassToDBPass(loginVo.getPassword(), spikeUser.getSalt());
        if (!spikeUser.getPassword().equals(calculatedPassword)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        redisService.set(SpikeUserPrefix.token, token, spikeUser);
        reviseCookie(response, COOKIE_NAME_TOKEN, token, SpikeUserPrefix.token.expireSeconds());
        return token;
    }

    private void reviseCookie(HttpServletResponse response, String key, String value, int expiredSeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public boolean updatePassword(String token, long id, String formPass) {//更新数据，如果数据使用缓存，一定要更新缓存
        //DB取user
        SpikeUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //先更新数据库
        SpikeUser toBeUpdate = new SpikeUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        spikeUserDao.update(toBeUpdate);
        //再处理缓存
        redisService.delete(SpikeUserPrefix.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SpikeUserPrefix.token, token, user);//更新修改的user信息，同时保持用户登录状态，使用同一token
        return true;
    }
}



























