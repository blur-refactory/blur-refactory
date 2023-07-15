package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatroomDto;
import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;
import com.blur.bluruser.chat.repository.ChatroomRepository;
import com.blur.bluruser.profile.entity.UserProfile;
import com.blur.bluruser.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void makeChatroom(MakeChatroomDto makeChatroomDto) {
        UserProfile maleProfile = userProfileRepository.findByUserId(makeChatroomDto.getMaleId());
        UserProfile femaleProfile = userProfileRepository.findByUserId(makeChatroomDto.getFemaleId());

        if (maleProfile != null && femaleProfile != null) {
            int maleIndex = maleProfile.getUserId().indexOf("@");
            String maleId = maleProfile.getUserId().substring(0, maleIndex);

            int femaleIndex = femaleProfile.getUserId().indexOf("@");
            String femaleId = femaleProfile.getUserId().substring(0, femaleIndex);

            Chatroom chatroom = Chatroom.builder()
                    .id(maleId + femaleId)
                    .maleName(maleProfile.getNickname())
                    .femaleName(femaleProfile.getNickname())
                    .maleId(makeChatroomDto.getMaleId())
                    .femaleId(makeChatroomDto.getFemaleId())
                    .build();

            chatroomRepository.save(chatroom);
        }
    }

    //    @Override
//    public List<Chatroom> getChatroom(String userId) {
//        UserProfile userProfile = userProfileRepository.findByUserId(userId);
//
//        if (userProfile != null) {
//            if (userProfile.getGender().equals("M")) {
//                List<Chatroom> chatroomList = chatroomRepository.findByMaleId(userId)
//                        .orElse(Collections.emptyList());
//                log.info("상대 유저 목록 {}", chatroomList);
//                return chatroomList;
//            } else {
//                List<Chatroom> chatroomList = chatroomRepository.findByFemaleId(userId)
//                        .orElse(Collections.emptyList());
//                log.info("상대 유저 목록 {}", chatroomList);
//                return chatroomList;
//            }
//        }
//        log.info("상대 유저 없음");
//        return Collections.emptyList();
//    }
    @Override
    public List<ChatroomDto> getChatroom(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);

        if (userProfile != null) {
            List<Chatroom> chatroomList;
            if (userProfile.getGender().equals("M")) {
                chatroomList = chatroomRepository.findByMaleId(userId)
                        .orElse(Collections.emptyList());
            } else {
                chatroomList = chatroomRepository.findByFemaleId(userId)
                        .orElse(Collections.emptyList());
            }

            List<ChatroomDto> chatroomDtoList = new ArrayList<>();
            for (Chatroom chatroom : chatroomList) {
                ChatroomDto chatroomDto = new ChatroomDto();
                chatroomDto.setId(chatroom.getId());
                if (chatroom.getMaleId().equals(userId)) {
                    chatroomDto.setMyName(chatroom.getMaleName());
                    chatroomDto.setMyId(chatroom.getMaleId());
                    chatroomDto.setOpponentName(chatroom.getFemaleName());
                    chatroomDto.setOpponentId(chatroom.getFemaleId());
                } else {
                    chatroomDto.setMyName(chatroom.getFemaleName());
                    chatroomDto.setMyId(chatroom.getFemaleId());
                    chatroomDto.setOpponentName(chatroom.getMaleName());
                    chatroomDto.setOpponentId(chatroom.getMaleId());
                }
                chatroomDtoList.add(chatroomDto);
            }
            log.info("상대 유저 목록 {}", chatroomDtoList);
            return chatroomDtoList;
        }
        log.info("상대 유저 없음");
        return Collections.emptyList();
    }
}
