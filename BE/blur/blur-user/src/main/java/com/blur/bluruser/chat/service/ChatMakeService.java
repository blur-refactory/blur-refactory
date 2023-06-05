package com.blur.bluruser.chat.service;

import com.blur.chat.api.dto.ChatDto;
import com.blur.chat.api.dto.MakeChatroomDto;
import com.project.smg.alarm.dto.AlarmDto;
import com.project.smg.alarm.dto.LatestAlarmsResultDto;

public interface ChatMakeService {
    void makeChatroom(MakeChatroomDto makeChatroomDto);
    ChatDto saveChat(String memberId, int id, String nickname);
    AlarmDto savePodoAlarm(String memberId);
    LatestAlarmsResultDto alarmDtoList(String memberId, double lastSocre);
    boolean deleteAlarm(String memberId, double deleteStart, double deleteEnd);
}
