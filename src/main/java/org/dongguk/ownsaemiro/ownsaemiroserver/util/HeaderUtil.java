package org.dongguk.ownsaemiro.ownsaemiroserver.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.JwtTokenDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class HeaderUtil {
    public static Optional<String> refineHeader (HttpServletRequest request, String headerName, String prefix){
        String headerValue = request.getHeader(headerName);
        if (!StringUtils.hasText(headerValue) || !headerValue.startsWith(prefix))
            throw new CommonException(ErrorCode.INVALID_HEADER_VALUE);
        return Optional.of(headerValue.substring(prefix.length()));
    }

    public static void addHeader(HttpServletResponse response, String headerName, String value){
        response.addHeader(headerName, value);
    }

    public static void addHeaders(HttpServletResponse response, JwtTokenDto jwtTokenDto){
        addHeader(response, Constants.ACCESS_COOKIE_NAME,jwtTokenDto.accessToken());
        addHeader(response, Constants.REFRESH_COOKIE_NAME,jwtTokenDto.refreshToken());
    }
}
