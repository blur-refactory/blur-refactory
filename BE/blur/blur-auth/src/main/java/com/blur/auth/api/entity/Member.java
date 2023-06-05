package com.blur.auth.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "member")
@AllArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id", unique = true)
    @NotNull
    private String id;

    @JsonIgnore
    private String password;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
