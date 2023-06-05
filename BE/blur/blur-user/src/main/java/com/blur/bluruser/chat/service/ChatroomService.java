package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;

public interface ChatroomService {
    void makeChatroom(MakeChatroomDto makeChatroomDto);
    Chatroom getChatroom(String email);
}
