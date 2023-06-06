package com.blur.auth.api.service;

import com.blur.auth.api.dto.UserSignUpDto;
import com.blur.auth.api.entity.User;
import com.blur.auth.api.dto.Role;
import com.blur.auth.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserSignUpDto userSignUpDto) throws Exception {
        log.info("유저이메일 들어오는지 확인", userSignUpDto.getEmail() );
        if (userRepository.findById(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일");
        }

        User user = User.builder()
                .role(Role.GUEST)
                .id(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    public Boolean checkId(String userId) {
        User User = userRepository.findById(userId)
                .orElse(null);
        if (User != null) {
            return false;
        }
        return true;
    }

    public String getEmail(String userId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null)
            return null;

        String userEmail = user.getId();
        return userEmail;
    }
}
