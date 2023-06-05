package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatDto;
import com.project.smg.alarm.dto.LatestAlarmsResultDto;

public interface ChatMakeService {
    ChatDto saveChat(String memberId, int id, String nickname);
    LatestAlarmsResultDto alarmDtoList(String memberId, double lastSocre);
    boolean deleteAlarm(String memberId, double deleteStart, double deleteEnd);
}
