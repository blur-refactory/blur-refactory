package com.blur.bluruser.chat.controller;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import com.blur.bluruser.chat.entity.Chatroom;
import com.blur.bluruser.chat.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatroomService chatroomService;

    @PostMapping("/createChatroom")
    public ResponseEntity<?> createChatroom(@RequestBody MakeChatroomDto makeChatroomDto) {
        chatroomService.makeChatroom(makeChatroomDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getChatrooms")
    public ResponseEntity<?> getChatrooms(@RequestHeader("X-Username") String userId) {
        List<Chatroom> chatroomList = chatroomService.getChatroom(userId);
        if (!chatroomList.isEmpty())
            return new ResponseEntity<>(chatroomList, HttpStatus.OK);
        else
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
}
