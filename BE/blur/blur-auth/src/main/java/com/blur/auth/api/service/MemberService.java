package com.blur.auth.api.service;

import com.blur.auth.api.dto.MemberSignUpDto;
import com.blur.auth.api.entity.Member;
import com.blur.auth.api.entity.Role;
import com.blur.auth.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(MemberSignUpDto memberSignUpDto) throws Exception{
        if(memberRepository.findByEmail(memberSignUpDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일");
        }

        Member member = Member.builder()
                .role(Role.GUEST)
                .email(memberSignUpDto.getEmail())
                .password(memberSignUpDto.getPassword())
                .id(UUID.randomUUID().toString())
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }

    public Boolean checkId(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElse(null);
        if (member != null) {
            return false;
        }
        return true;
    }

    public String getEmail(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElse(null);

        if (member == null)
            return null;

        String userEmail = member.getEmail();
        return userEmail;
    }
}
