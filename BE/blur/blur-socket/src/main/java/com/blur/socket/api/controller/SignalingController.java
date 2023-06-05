package com.blur.socket.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.blur.socket.api.entity.SignalingMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/offer")
    public void handleOffer(@Payload SignalingMessage message) {
        // Offer 메시지 처리
        // ...
    	log.info("Received offer message from roomId {} : {}", message.getRoomId(), message.getMessage());
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }

    @MessageMapping("/answer")
    public void handleAnswer(@Payload SignalingMessage message) {
        // Answer 메시지 처리
        // ...
        log.info("Received answer message from roomId {} : {}", message.getRoomId(), message.getMessage());
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }

    @MessageMapping("/iceCandidate")
    public void handleIceCandidate(@Payload SignalingMessage message) {
        // ICE Candidate 메시지 처리
        // ...
        log.info("Received iceCandidate message from roomId {} : {}", message.getRoomId(), message.getMessage());
        messagingTemplate.convertAndSend("/topic/" + message.getRoomId(), message);
    }
}
