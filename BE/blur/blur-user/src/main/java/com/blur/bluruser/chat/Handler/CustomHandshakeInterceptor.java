package com.blur.bluruser.chat.Handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * WebSocket 핸드셰이크 전에 호출되는 메서드입니다.
     *
     * @param request    현재 요청
     * @param response   현재 응답
     * @param wsHandler  대상 WebSocket 핸들러
     * @param attributes WebSocket과 연결할 HTTP 핸드셰이크의 속성들이 담긴 맵입니다.
     *                   제공된 속성은 복사되며, 원래의 맵은 사용되지 않습니다.
     * @return WebSocket 핸드셰이크를 계속 진행할지 여부를 나타내는 boolean 값입니다.
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("HandshakeInterceptor 시작");
        if (request instanceof ServletServerHttpRequest) {
            // ServletServerHttpRequest를 사용하여 HttpServletRequest를 가져옴
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
//            String userId = (String) httpServletRequest.getAttribute("id");
//            String roomId = (String) httpServletRequest.getAttribute("roodId");
            String userId = httpServletRequest.getHeader("X-Username");
            String roomId = extractRoomIdFromQuery(httpServletRequest.getQueryString());

            log.info("아이디 {}", userId);
            log.info("방번호 {}", roomId);

            if (userId != null && roomId != null) {
                // "id" 속성을 attributes 맵에 추가
                attributes.put("id", userId);
                attributes.put("roomId", roomId);
                return true;
            }
        }

        log.info("연결 실패");
        return false;
    }

    /**
     * WebSocket 핸드셰이크 후에 호출되는 메서드입니다.
     *
     * @param request   현재 요청
     * @param response  현재 응답
     * @param wsHandler 대상 WebSocket 핸들러
     * @param exception 예외 (발생하지 않았을 경우 null)
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("WebSocket 연결이 수립되었습니다.");
    }

    private String extractRoomIdFromQuery(String query) {
        String[] parameters = query.split("&");
        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("roomId")) {
                return keyValue[1];
            }
        }
        return null;
    }
}
