package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatroomDto;
import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;

import java.util.List;

public interface ChatroomService {
    void makeChatroom(MakeChatroomDto makeChatroomDto);
    List<ChatroomDto> getChatroom(String email);
}
