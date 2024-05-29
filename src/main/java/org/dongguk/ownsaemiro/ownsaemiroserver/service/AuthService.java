package org.dongguk.ownsaemiro.ownsaemiroserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.domain.User;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.request.TestAuthSignUpDto;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.type.ERole;
import org.dongguk.ownsaemiro.ownsaemiroserver.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Transactional
    public void signUp(TestAuthSignUpDto authSignUpDto){
        userRepository.save(
                User.signUp(authSignUpDto, passwordEncoder.encode(authSignUpDto.password()), ERole.toERole(authSignUpDto.role()))
        );
        //TODO: 사용자 지갑 생성 로직 필요
    }
}
