package org.dongguk.ownsaemiro.ownsaemiroserver.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.dongguk.ownsaemiro.ownsaemiroserver.security.info.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRepository.UserSecurityForm securityForm = userRepository.findUserSecurityFromBySerialId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
        log.info("아이디 기반 조회 성공");
        return UserPrincipal.create(securityForm);
    }
    public UserPrincipal loadUserById(Long id) {
        UserRepository.UserSecurityForm userSecurityForm = userRepository.findUserSecurityFromById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        log.info("user id 기반 조회 성공");

        return UserPrincipal.create(userSecurityForm);
    }
}
