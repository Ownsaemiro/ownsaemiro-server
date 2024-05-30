package org.dongguk.ownsaemiro.ownsaemiroserver.security.info;

import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONValue;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.global.ExceptionDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.JwtTokenDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.CookieUtil;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.HeaderUtil;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationResponse {
    public static void makeLoginSuccessResponse(HttpServletResponse response, JwtTokenDto jwtTokenDto, Integer refreshExpiration) throws IOException {
        CookieUtil.addCookie(response, Constants.ACCESS_COOKIE_NAME, jwtTokenDto.accessToken());
        CookieUtil.addSecureCookie(response, Constants.REFRESH_COOKIE_NAME, jwtTokenDto.refreshToken(), refreshExpiration);

        makeSuccessResponse(response);

    }
    public static void makeOauthSuccessResponse(HttpServletResponse response, JwtTokenDto jwtTokenDto) throws IOException {
        HeaderUtil.addHeaders(response, jwtTokenDto);
        makeSuccessResponse(response);

    }
    public static void makeLoginSuccessResponse(HttpServletResponse response, String domain, JwtTokenDto jwtTokenDto, Integer refreshExpiration) throws IOException {
        CookieUtil.addCookie(response, domain, Constants.ACCESS_COOKIE_NAME, jwtTokenDto.accessToken());
        CookieUtil.addSecureCookie(response, domain, Constants.REFRESH_COOKIE_NAME, jwtTokenDto.refreshToken(), refreshExpiration);

        makeSuccessResponse(response);

    }
    public static void makeSuccessResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());

        Map<String, Object> body = new HashMap<>();
        body.put("success", "true");
        body.put("data", null);
        body.put("error", null);

        response.getWriter().write(JSONValue.toJSONString(body));
    }

    public static void makeFailureResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        Map<String, Object> body= new HashMap<>();
        body.put("success", false);
        body.put("data", null);
        body.put("error", ExceptionDto.of(errorCode));

        response.getWriter().write(JSONValue.toJSONString(body));
    }
}
