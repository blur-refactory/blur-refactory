package com.blur.auth.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
<<<<<<< HEAD
//        return (String) attributes.get("account_email");
        return String.valueOf(attributes.get("account_email"));
    }
=======
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) account.get("email");
>>>>>>> b468e6c02fd2fc822872bc8134e6b865d374e42b

        if (account == null || email == null) {
            return null;
        }

        return email;
    }
}
