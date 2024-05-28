package org.dongguk.ownsaemiro.ownsaemiroserver.security.handler.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.JwtTokenDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.info.AuthenticationResponse;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.info.UserPrincipal;
import org.dongguk.ownsaemiro.ownsaemiroserver.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        JwtTokenDto jwtTokenDto = jwtUtil.generateTokens(principal.getUserId(), principal.getRole());

        AuthenticationResponse.makeLoginSuccessResponse(response, jwtTokenDto, jwtUtil.getRefreshExpiration());

        response.sendRedirect("http://localhost:3000");
    }
}
