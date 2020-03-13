package com.twigle.spike.config;

import com.twigle.spike.exception.GlobalException;
import com.twigle.spike.model.SpikeUser;
import com.twigle.spike.result.CodeMsg;
import com.twigle.spike.service.SpikeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    SpikeUserService spikeUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType() == SpikeUser.class;
    }

    @Override
    public SpikeUser resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String requestParameter = request.getParameter(SpikeUserService.COOKIE_NAME_TOKEN);
        String cookieParameter = getCookieValue(request, SpikeUserService.COOKIE_NAME_TOKEN);
        String token;
        if (!StringUtils.isEmpty(requestParameter)) {
            token = requestParameter;
        } else if (!StringUtils.isEmpty(cookieParameter)) {
            token = cookieParameter;
        } else
            return null;
        SpikeUser spikeUser = spikeUserService.getByToken(response, token);
        if (null == spikeUser) throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        return spikeUser;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                return cookie.getValue();
        }
        return null;
    }
}
