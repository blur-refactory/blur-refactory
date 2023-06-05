package com.blur.bluruser.chat.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUtils {
    // 채팅 데이터 생성일자 Double 형으로 형변환
    public Double changeLocalDateTimeToDouble(LocalDateTime createdAt) {
        return ((Long) createdAt
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli())
                .doubleValue();
    }
}
