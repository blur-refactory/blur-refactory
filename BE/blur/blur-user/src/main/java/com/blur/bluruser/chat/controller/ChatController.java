package com.blur.bluruser.chat.controller;

import com.blur.bluruser.chat.dto.MakeChatroomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    @PostMapping("/createChatroom")
    public ResponseEntity<?> createChatroom(@RequestBody MakeChatroomDto makeChatroomDto){

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
