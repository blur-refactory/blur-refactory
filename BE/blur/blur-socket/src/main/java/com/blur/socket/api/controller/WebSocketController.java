package com.blur.socket.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.blur.socket.api.entity.ChatDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class WebSocketController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final Logger log = LoggerFactory.getLogger(WebSocketController.class);

	@MessageMapping("/chat")
	public void sendMessage(ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
		log.info("Received message from {} in channel {}: {}", chatDto.getChat(), chatDto.getChannelId(),
				chatDto.getWriterId());
		simpMessagingTemplate.convertAndSend("/sub/chat/" + chatDto.getChannelId(), chatDto);
	}
}
