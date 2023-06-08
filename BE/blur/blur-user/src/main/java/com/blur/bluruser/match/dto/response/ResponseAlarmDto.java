package com.blur.bluruser.match.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ResponseAlarmDto {

    private Long alarmId;

    private String sessionId;
}

