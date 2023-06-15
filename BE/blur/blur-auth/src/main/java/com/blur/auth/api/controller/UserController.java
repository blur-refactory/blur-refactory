package com.blur.auth.api.controller;

import com.blur.auth.api.dto.*;
import com.blur.auth.api.service.EmailService;
import com.blur.auth.api.service.PasswordService;
import com.blur.auth.api.service.UserService;
import com.blur.auth.jwt.service.JwtService;
import com.blur.auth.utils.error.CustomException;
import com.blur.auth.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordService passwordService;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<UserSignUpRes> register(@RequestBody UserSignUpReq userSignUpReq) throws Exception {
        ResponseEntity<UserSignUpRes> userSignUpRes = userService.register(userSignUpReq);
        return userSignUpRes;
    }

    @PostMapping("/login")
    public ResponseEntity normalLogin(@RequestBody UserLoginReq userLoginReq) throws Exception {
        return userService.normalLogin(userLoginReq);
    }

    @PostMapping("/sendAuthEmail") // 이메일 인증메일 발송
    @CrossOrigin
    public SendAuthEmailRes sendAuthEmail(@RequestBody SendAuthEmailReq sendAuthEmailReq) throws Exception {
        String email = sendAuthEmailReq.getEmail();
        return emailService.sendAuthMessage(email);
    }

    @PostMapping("/checkId") // 아이디 중복체크
    public ResponseEntity<Boolean> checkId(@RequestBody Map<String, String> param) {
        String userId = param.get("userId");
        Boolean res = userService.checkId(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> param){
        String email = param.get("email");
        String authKey = param.get("authKey");
        if (emailService.getAuthKey(email, authKey))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new CustomException(ErrorCode.BAD_REQUEST);
    }


    @PutMapping("/findPassword") // 비밀번호 찾기
    public ResponseEntity<Boolean> findPassword(@RequestBody Map<String, String> param) throws Exception {
        String userId = param.get("userId");
        Boolean res = passwordService.sendTempPassword(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    @CrossOrigin
    public ChangePasswordRes changePassword(@RequestBody ChangePasswordReq changePasswordReq) {
        return userService.changePassword(changePasswordReq);
    }

//    @PostMapping("userInfo/{userId}")
//    public ResponseEntity<?> getUserInfo(@PathVariable(name ="userId") String userId) throws Exception{
//        UserInfo userInfo = userService.getUserInfo(userId);
//        Long userNo = memberService.getUserInfo(userId).getUserNo();
//        return new ResponseEntity<>(userNo, HttpStatus.OK);
//    }


//    @GetMapping("/getEmail")
//    public ResponseEntity<?> getEmail(@RequestParam("userId") String userId) throws Exception {
//        String userEmail = userService.getEmail(userId);
//        return ResponseEntity.status(HttpStatus.OK).body(userEmail);
//    }


}
