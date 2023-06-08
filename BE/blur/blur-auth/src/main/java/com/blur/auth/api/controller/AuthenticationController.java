package com.blur.auth.api.controller;

import com.blur.auth.jwt.service.JwtService;
import com.blur.auth.utils.error.CustomException;
import com.blur.auth.utils.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtService jwtService;

    @GetMapping("/validate")
    public ResponseEntity<String> checkAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        System.out.println(userId);
        if (userId == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        } else {
            return ResponseEntity.ok()
                    .header("X-Username", userId)
                    .build();
        }

//        Optional<String> jwtToken = jwtService.extractAccessToken(request);
//        if(jwtToken.isPresent()){
//            String token = jwtToken.get();
//            log.info("jwtToken", token);
//            Boolean isTokenValid = jwtService.isTokenValid(token);
//            if (isTokenValid == true) {
//                String userId = jwtService.getUserIdFromToken(token);
//                log.info("userId", userId);
//                return ResponseEntity.ok()
//                        .header("X-Username", userId)
//                        .build();
//            } else {
//                log.error("토큰값", token);
//                throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
//            }
//        } else {
//            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
//        }
    }
}
