package com.blur.auth.oauth2.handler;

import com.blur.auth.api.entity.Member;
import com.blur.auth.api.entity.RefreshToken;
import com.blur.auth.api.repository.MemberRepository;
import com.blur.auth.api.repository.RefreshTokenRepository;
import com.blur.auth.jwt.service.JwtService;
import com.blur.auth.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private static final String home = "https://www.shinemustget.com/home";
    private static final String create = "https://www.shinemustget.com/create";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, DefaultOAuth2User oAuth2User) throws IOException {
        String memberId = oAuth2User.getAttributes().get("id").toString();
        Optional<Member> findMember = memberRepository.findById(memberId);
        Member member = findMember.orElseThrow(() -> new IllegalStateException("유저가 존재하지 않음"));

        //토큰 검증 과정
        refreshTokenRepository.findByMemberEmail(member.getEmail())
                .ifPresentOrElse(refreshToken -> {
                            log.info("Refresh Token 있음");
                            String accessToken = jwtService.createAccessToken(memberId);
                            jwtService.setAccessTokenHeader(response, accessToken);
                            jwtService.refreshTokenAddCookie(response, refreshToken.getRefreshToken());
                        },
                        () -> {
                            log.info("Refresh Token 없음");
                            String accessToken = jwtService.createAccessToken(memberId);
                            jwtService.setAccessTokenHeader(response, accessToken);
                            String newRefreshToken = jwtService.createRefreshToken();
                            jwtService.refreshTokenAddCookie(response, newRefreshToken);
                            RefreshToken token = RefreshToken.builder()
                                    .refreshToken(newRefreshToken)
                                    .member(member)
                                    .build();
                            refreshTokenRepository.save(token);
                        });

        // 만다라트 생성 시 유저 상태 변경
        if (member.getRole().getKey() == "ROLE_GUEST") {
            response.sendRedirect(create);
        } else {
            response.sendRedirect(home);
        }

    }
}