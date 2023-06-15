package com.blur.auth.api.service;

import com.blur.auth.api.dto.*;
import com.blur.auth.api.entity.RefreshToken;
import com.blur.auth.api.entity.User;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public ResponseEntity<UserSignUpRes> register(UserSignUpReq userSignUpReq) throws Exception {
        log.info("유저이메일 들어오는지 확인", userSignUpReq.getEmail());
        if (userRepository.findById(userSignUpReq.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT);
        }

        User user = User.builder()
                .role(Role.GUEST)
                .id(userSignUpReq.getEmail())
                .password(userSignUpReq.getPassword())
                .build();
        System.out.println(userSignUpReq.getPassword());
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

    public ResponseEntity normalLogin(UserLoginReq userLoginReq) {
        String givenEmail = userLoginReq.getEmail();
        String givenPassword = userLoginReq.getPassword();
        System.out.println(givenPassword);
//        String encodedPassword = passwordEncoder.encode(givenPassword);
        Optional<User> user = userRepository.findById(givenEmail);

        if (user.isPresent()) {
            User userData = user.get();
            if (passwordEncoder.matches(givenPassword, userData.getPassword())){
                String accessToken = jwtService.createAccessToken(userLoginReq.getEmail());
                String refreshToken = jwtService.createRefreshToken();
                Optional<RefreshToken> currentRefreshToken = refreshTokenRepository.findByUserId(userData.getId());
                if (currentRefreshToken.isPresent()) {
                    RefreshToken newRefreshToken = currentRefreshToken.get();
                    newRefreshToken.setRefreshToken(refreshToken);
                    refreshTokenRepository.save(newRefreshToken);
                } else {
                    throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                }
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("accessToken", accessToken)
                        .build();
            }
        } else {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public Boolean checkId(String userId) {
        User User = userRepository.findById(userId)
                .orElse(null);
        if (User != null) {
            return false;
        }
        throw new CustomException(ErrorCode.CONFLICT);
    }

    public ChangePasswordRes changePassword(ChangePasswordReq changePasswordReq) {
        String userEmail = changePasswordReq.getEmail();
        String givenPassword = changePasswordReq.getPassword();
        Optional<User> user =  userRepository.findById(userEmail);
        if (user.isPresent()) {
            User userData = user.get();
            userData.setPassword(givenPassword);
            userData.passwordEncode(passwordEncoder);
            userRepository.save(userData);
            return ChangePasswordRes.builder()
                    .code("200")
                    .msg("정상적으로 비밀번호가 변경되었습니다.")
                    .build();
        } else {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public String getEmail(String userId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null)
            return null;

        String userEmail = user.getId();
        return userEmail;
    }



//    public String issueAccessAndRefreshToken(User user) {
//        String accessToken = jwtService.createAccessToken(user.getId());
//        String refreshToken = jwtService.createRefreshToken();
//        RefreshToken refreshTokenBuild = RefreshToken.builder()
//                .refreshToken(refreshToken)
//                .user(user)
//                .build();
//        refreshTokenRepository.save(refreshTokenBuild);
//    }
}
