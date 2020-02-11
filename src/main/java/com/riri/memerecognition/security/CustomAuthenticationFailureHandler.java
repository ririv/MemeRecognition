package com.riri.memerecognition.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException{

//        response.sendError(401,exception.getMessage());

        //自定义的json信息
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status",401.1);
        resultMap.put("message",exception.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(resultMap));
    }
}
