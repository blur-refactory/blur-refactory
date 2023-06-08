package com.blur.bluruser.chat.config;

import com.blur.bluruser.chat.Handler.CustomHandshakeInterceptor;
import com.blur.bluruser.chat.Handler.CustomWebSocketHandler;
import com.blur.bluruser.chat.service.ChatMakeService;
import com.blur.bluruser.chat.service.ChatSendService;
import com.blur.bluruser.chat.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatMakeService chatMakeService;
    private final ChatSendService chatSendService;
    private final ChatUtils chatUtils;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWebSocketHandler(), "/ws")
                .addInterceptors(customHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:8081", "http://blurblur.kr", "https://blurblur.kr");
    }

    @Bean
    public CustomWebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler(chatMakeService, chatSendService, chatUtils);
    }

    @Bean
    public HandshakeInterceptor customHandshakeInterceptor() {
        return new CustomHandshakeInterceptor();
    }
}
