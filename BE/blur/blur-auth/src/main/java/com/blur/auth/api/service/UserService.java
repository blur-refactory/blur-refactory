package com.blur.auth.api.service;

import com.blur.auth.api.dto.UserLoginReq;
import com.blur.auth.api.dto.UserSignUpReq;
import com.blur.auth.api.dto.UserSignUpRes;
import com.blur.auth.api.entity.RefreshToken;
import com.blur.auth.api.entity.User;
import com.blur.auth.api.dto.Role;
import com.blur.auth.api.repository.RefreshTokenRepository;
import com.blur.auth.api.repository.UserRepository;
import com.blur.auth.jwt.service.JwtService;
import com.blur.auth.utils.error.CustomException;
import com.blur.auth.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public ResponseEntity<UserSignUpRes> register(UserSignUpReq userSignUpReq) throws Exception {
        log.info("유저이메일 들어오는지 확인", userSignUpReq.getEmail() );
        if (userRepository.findById(userSignUpReq.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT);
        }

        User user = User.builder()
                .role(Role.GUEST)
                .id(userSignUpReq.getEmail())
                .password(userSignUpReq.getPassword())
                .build();
        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
        UserSignUpRes userSignUpRes = UserSignUpRes.builder()
                .email(userSignUpReq.getEmail())
                .build();

        String accessToken = jwtService.createAccessToken(userSignUpReq.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        RefreshToken refreshTokenBuild = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();
        refreshTokenRepository.save(refreshTokenBuild);
        return ResponseEntity.ok()
                .header("accessToken", accessToken)
                .body(userSignUpRes);
    }

//    public ResponseEntity<String> normalLogin(UserLoginReq userLoginReq) {
//        String email = userLoginReq.getEmail();
//        String password = userLoginReq.getPassword();
//        userRepository.findById(email);
//
//    }

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
