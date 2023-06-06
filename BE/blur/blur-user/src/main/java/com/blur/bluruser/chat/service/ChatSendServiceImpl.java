package com.blur.bluruser.chat.service;

import com.blur.bluruser.chat.Handler.CustomWebSocketHandler;
import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.SendChatDto;
import com.blur.bluruser.chat.entity.Chatroom;
import com.blur.bluruser.chat.repository.ChatroomRepository;
import com.blur.bluruser.chat.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSendServiceImpl implements ChatSendService {
    private final ChatMakeService chatMakeService;
    private final ChatroomRepository chatroomRepository;
    private final ChatUtils chatUtils;

    @Override
    public ChatDto sendChat(String userId, String roodId, String nickname, String message) throws Exception {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(roodId);

        if (optionalChatroom.isPresent()) {
            ChatDto chatDto = chatMakeService.saveChat(userId, roodId, message);
            return chatDto;
        } else {
            log.warn("존재하지 않는 채팅방");
            return null;
        }
    }
}
