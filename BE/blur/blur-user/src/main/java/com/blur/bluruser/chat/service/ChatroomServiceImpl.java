package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;
import com.blur.bluruser.chat.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService{
    private final ChatroomRepository chatroomRepository;

    @Override
    public void makeChatroom(MakeChatroomDto makeChatroomDto) {
        Chatroom chatroom = Chatroom.builder()
                .maleName(makeChatroomDto.getMaleName())
                .femaleName(makeChatroomDto.getFemaleName())
                .maleEmail(makeChatroomDto.getMaleEmail())
                .femaleEmail(makeChatroomDto.getFemaleEmail())
                .build();

        chatroomRepository.save(chatroom);
    }

    @Override
    public Chatroom getChatroom(String email) {
        Chatroom chatroom = chatroomRepository.findByMaleEmail(email)
                .orElse(null);

        return chatroom;
    }
}
