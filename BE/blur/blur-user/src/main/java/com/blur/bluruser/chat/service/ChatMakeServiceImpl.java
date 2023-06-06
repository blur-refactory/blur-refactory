package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;
import com.blur.bluruser.chat.dto.ReceiveDto;
import com.blur.bluruser.chat.entity.Chatroom;
import com.blur.bluruser.chat.repository.ChatroomRepository;
import com.blur.bluruser.chat.repository.RedisChatRepository;
import com.blur.bluruser.profile.entity.UserProfile;
import com.blur.bluruser.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMakeServiceImpl implements ChatMakeService {
    private final RedisChatRepository redisChatRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserProfileRepository userProfileRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm");

    @Override
    public ChatDto saveChat(String userId, String roodId, ReceiveDto receiveDto) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(roodId);
//        UserProfile userProfile = userProfileRepository.findByUserId(userId);

        if (optionalChatroom.isPresent()) {
            LocalDateTime time = LocalDateTime.now();

            ChatDto chatDto = ChatDto.builder()
                    .userId(userId)
                    .nickname(receiveDto.getNickname())
                    .message(receiveDto.getMessage())
                    .roomId(roodId)
                    .createdAt(time)
                    .formattedCreatedAt(time.format(FORMATTER))
                    .build();

            redisChatRepository.save(chatDto);
            return chatDto;
        } else {
            log.warn("존재하지 않는 채팅방");
            return null;
        }
    }

    @Override
    public LatestChatsResultDto chatDtoList(String roomId, double lastSocre) {
        return redisChatRepository.getLatestChats(roomId, lastSocre);
    }

    @Override
    public boolean deleteChat(String roomId, double deleteStart, double deleteEnd) {
        return redisChatRepository.delete(roomId, deleteStart, deleteEnd);
    }
}
