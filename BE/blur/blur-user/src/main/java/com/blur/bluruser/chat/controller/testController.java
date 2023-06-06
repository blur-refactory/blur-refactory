package com.blur.bluruser.chat.controller;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.dto.ReceiveDto;
import com.blur.bluruser.chat.service.ChatMakeService;
import com.blur.bluruser.chat.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class testController {

    private final ChatroomService chatroomService;
    private final ChatMakeService chatMakeService;

    @PostMapping("/test")
    public ResponseEntity<?> createChatroom(@RequestBody MakeChatroomDto makeChatroomDto) {
        chatroomService.makeChatroom(makeChatroomDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/make")
    public ResponseEntity<?> test(@RequestBody Map<String, String> testMap) {
        String userId = testMap.get("userId");
        String roodId = testMap.get("roomId");
        String nickname = testMap.get("nickname");
        String message = testMap.get("message");
        ReceiveDto receiveDto = ReceiveDto.builder()
                .nickname(nickname)
                .message(message)
                .build();
        chatMakeService.saveChat(userId, roodId, receiveDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
