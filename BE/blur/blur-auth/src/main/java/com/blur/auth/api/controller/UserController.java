package com.blur.auth.api.controller;

import com.blur.auth.api.dto.UserSignUpReq;
import com.blur.auth.api.dto.UserSignUpRes;
import com.blur.auth.api.repository.UserRepository;
import com.blur.auth.api.service.EmailService;
import com.blur.auth.api.service.PasswordService;
import com.blur.auth.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//TODO: 전반적으로 DTO를 구성하지 않는 상황이 있는데, Dto를 좀 많이 고쳐야 할 것 같다.
public class UserController {
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordService passwordService;
//    private final UserRepository userRepository;
//    @Value("${jwt.secretKey}")
//    private final String secretKey;

    @PostMapping("/register")
    public ResponseEntity<UserSignUpRes> register(@RequestBody UserSignUpReq userSignUpReq) throws Exception {
        ResponseEntity<UserSignUpRes> userSignUpRes = userService.register(userSignUpReq);
        return userSignUpRes;
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

    //유저 검증 계속 이걸 거쳐갈꺼임.
//    @GetMapping("/validate")
//    public ResponseEntity<String> checkAccessToken(@RequestHeader Map<String, String> headers) {
//        if (!headers.containsKey("authorization") || !headers.get("authorization").startsWith("Bearer ")) {
//            return ResponseEntity.badRequest().body("Missing or invalid authorization header");
//        }
//        headers.forEach((key,val)->{
//            System.out.println(String.format("%s=%s",key, val));
//        });
//        String accessToken = headers.get("authorization").replace("Bearer ", "");
//        String userId = null;
////        String secretKey = env.getProperty("jwt.secret");
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(secretKey.getBytes())
//                    .build()
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//            userId = claims.getSubject();
//            Date expiration = claims.getExpiration();
//            System.out.println(userId);
//            System.out.println(expiration);
//            if (expiration.before(new Date())) {
//                throw new Exception();
//            }
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
//        }
//        if (userId == null || userId.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
//        }
//        Optional<User> user = userRepository.findById(userId);
//        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
//        return ResponseEntity.ok()
//                .header("X-Username", userId)
//                .build();
//    }
}
