package com.blur.auth.api.controller;

import com.blur.auth.api.dto.UserSignUpDto;
import com.blur.auth.api.service.EmailService;
import com.blur.auth.api.service.PasswordService;
import com.blur.auth.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordService passwordService;

    @PostMapping("/register")
    public ResponseEntity<?> register(UserSignUpDto memberSignUpDto) throws Exception {
        userService.register(memberSignUpDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sendAuthEmail") // 이메일 인증메일 발송
    public ResponseEntity<?> sendAuthEmail(@RequestBody Map<String, String> param) throws Exception {
        String email = param.get("email");
        emailService.sendAuthMessage(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/checkId") // 아이디 중복체크
    public ResponseEntity<Boolean> checkId(@RequestBody Map<String, String> param) {
        String userId = param.get("userId");
        Boolean res = userService.checkId(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> param) throws Exception {
        String email = param.get("email");
        String authKey = param.get("authKey");

        if (emailService.getAuthKey(email, authKey))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/findPassword") // 비밀번호 찾기
    public ResponseEntity<Boolean> findPassword(@RequestBody Map<String, String> param) throws Exception {

        String userId = param.get("userId");
        Boolean res = passwordService.sendTempPassword(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

//    @PostMapping("userInfo/{userId}")
//    public ResponseEntity<?> getUserInfo(@PathVariable(name ="userId") String userId) throws Exception{
//        UserInfo userInfo = userService.getUserInfo(userId);
//        Long userNo = memberService.getUserInfo(userId).getUserNo();
//        return new ResponseEntity<>(userNo, HttpStatus.OK);
//    }

    @GetMapping("/getEmail")
    public ResponseEntity<?> getEmail(@RequestParam("userId") String userId) throws Exception {
        String userEmail = userService.getEmail(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userEmail);
    }
}
