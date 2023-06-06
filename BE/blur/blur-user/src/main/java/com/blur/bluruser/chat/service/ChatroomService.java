package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;

import java.util.List;

public interface ChatroomService {
    void makeChatroom(MakeChatroomDto makeChatroomDto);
    List<Chatroom> getChatroom(String email);
}
