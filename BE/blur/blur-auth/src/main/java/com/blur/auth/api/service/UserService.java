package com.blur.auth.api.service;

import com.blur.auth.api.dto.UserSignUpDto;
import com.blur.auth.api.entity.User;
import com.blur.auth.api.entity.Role;
import com.blur.auth.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserSignUpDto UserSignUpDto) throws Exception {
        if (userRepository.findById(UserSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일");
        }

        User user = User.builder()
                .role(Role.GUEST)
                .id(UserSignUpDto.getEmail())
                .password(UserSignUpDto.getPassword())
                .id(UUID.randomUUID().toString())
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
