package com.blur.auth.api.service;

import com.blur.auth.api.entity.User;
import com.blur.auth.api.repository.UserRepository;
import com.blur.auth.config.email.EmailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;
    @Value("${mail.username}")
    private String userName;


    public static String createTempPassword() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    private String createMessage(String to, String tPw) throws Exception {
        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 Blur입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += tPw + "</strong><div><br/> ";
        msgg += "</div>";

        return msgg;
    }

    public Boolean sendTempPassword(String userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return false;
        } else {
            String to = user.getId();
            String tPw = createTempPassword();
            String message = createMessage(to, tPw);
            EmailHandler emailHandler = new EmailHandler(mailSender);
            emailHandler.setTo(to);
            emailHandler.setSubject("비밀번호 변경 테스트");
            emailHandler.setText(message, true);//내용
            emailHandler.setFrom(userName);//보내는 사람
            try {//예외처리
                emailHandler.send();
                user.updatePassword(passwordEncoder.encode(tPw));
                userRepository.save(user);
                return true;
            } catch (MailException es) {
                es.printStackTrace();
                throw new IllegalArgumentException();
            }
        }
    }

    public void updatePassword(String userId, String newPassword) throws Exception {
        User user = userRepository.findById(userId)
                .orElse(null);

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        System.out.println("비밀번호 변경");
    }
}
