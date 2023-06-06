package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;
import com.blur.bluruser.chat.repository.ChatroomRepository;
import com.blur.bluruser.profile.entity.UserProfile;
import com.blur.bluruser.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService{
    private final ChatroomRepository chatroomRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void makeChatroom(MakeChatroomDto makeChatroomDto) {
        UserProfile maleProfile = userProfileRepository.findByUserId(makeChatroomDto.getMaleId());
        UserProfile femaleProfile = userProfileRepository.findByUserId(makeChatroomDto.getFemaleId());

        if(maleProfile != null && femaleProfile != null){
            int maleIndex = maleProfile.getUserId().indexOf("@");
            String maleId = maleProfile.getUserId().substring(0, maleIndex);

            int femaleIndex = femaleProfile.getUserId().indexOf("@");
            String femaleId = femaleProfile.getUserId().substring(0, femaleIndex);

            Chatroom chatroom = Chatroom.builder()
                    .id(maleId + femaleId)
                    .maleName(maleProfile.getNickname())
                    .femaleName(femaleProfile.getNickname())
                    .maleEmail(makeChatroomDto.getMaleId())
                    .femaleEmail(makeChatroomDto.getFemaleId())
                    .build();

            chatroomRepository.save(chatroom);
        }
    }

    @Override
    public Chatroom getChatroom(String email) {
        Chatroom chatroom = chatroomRepository.findByMaleEmail(email)
                .orElse(null);

        return chatroom;
    }
}
