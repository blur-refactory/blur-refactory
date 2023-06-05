package com.blur.bluruser.chat.service;

public interface AlarmSendService {
    void sendLikeAlarm(String nickname, int id) throws Exception;
    void sendPodoAlarm(String memberId) throws Exception;
}
