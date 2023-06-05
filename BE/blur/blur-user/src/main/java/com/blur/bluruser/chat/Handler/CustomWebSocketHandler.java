package com.blur.bluruser.chat.Handler;

import com.blur.bluruser.chat.dto.LatestChatsResultDto;
import com.blur.bluruser.chat.dto.SendChatDto;
import com.blur.bluruser.chat.service.ChatMakeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, Map<String, WebSocketSession>> userSessions = new HashMap<>();
    private final ChatMakeService chatMakeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

//    public void addSession(String userId, WebSocketSession session) {
//        userSessions.computeIfAbsent(userId, key -> session);
//    }

    public void addSession(String userId, String roomId, WebSocketSession session) {
        Map<String, WebSocketSession> roomSessions = userSessions.computeIfAbsent(userId, key -> new HashMap<>());
        roomSessions.put(roomId, session);
    }

    public WebSocketSession getSession(String userId, String roomId) {
        Map<String, WebSocketSession> roomSessions = userSessions.get(userId);
        if (roomSessions != null) {
            return roomSessions.get(roomId);
        }
        return null;
    }

    public String getUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("id");
    }
    public String getRoomId(WebSocketSession session) {
        return (String) session.getAttributes().get("roomId");
    }

//    public void removeSession(String userId) {
//        if (userSessions.containsKey(userId)) {
//            userSessions.remove(userId);
//        }
//    }

    public void removeSession(String userId, String roomId) {
        Map<String, WebSocketSession> roomSessions = userSessions.get(userId);
        if (roomSessions != null) {
            roomSessions.remove(roomId);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        String roomId = getRoomId(session);
        log.info("소켓 연결");

        addSession(userId, roomId, session);
        log.info("연결한 유저 {}", userId);

        session.sendMessage(new TextMessage(sendAlarmList(chatMakeService.alarmDtoList(userId, 0.0), session)));

        log.info("메세지 조회 성공");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String jsonPayload = message.getPayload().toString();
        ReceiveDto receivedMessage = objectMapper.readValue(jsonPayload, ReceiveDto.class);
        log.info("메세지 수신 시작");

        String userId = getUserId(session);

        if (receivedMessage.getCursor() != null) {
            log.info("알람 전송");
            session.sendMessage(new TextMessage(sendAlarmList(chatMakeService.alarmDtoList(userId, Double.parseDouble(receivedMessage.getCursor())), session)));
        }

        if (receivedMessage.getDeleteStart() != null && receivedMessage.getDeleteEnd() != null) {
            log.info("알람 삭제 시도");
            boolean deleteResult = chatMakeService.deleteAlarm(userId, Double.parseDouble(receivedMessage.getDeleteStart()), Double.parseDouble(receivedMessage.getDeleteEnd()));
            if (deleteResult) {
                log.info("알람 삭제 성공");
            } else {
                log.info("알람 삭제 실패");
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        removeSession(userId);
        log.info("유저 로그아웃 {}", userId);
        log.info("소켓 연결 종료 {}", status);
        session.close();
    }

    /**
     * 이벤트 발생 시 알람 전송
     *
     * @param userId
     * @param sendChatDto
     * @throws Exception
     */
    public void sendMessageToUser(String userId, SendChatDto sendChatDto) throws Exception {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(sendChatDto)));
        } else {
            log.warn("수신자의 세션이 닫혀있거나 존재하지 않음 : {}", userId);
        }
    }

    /**
     * 알람 목록 전송
     *
     * @param latestChatsResultDto
     * @param session
     * @return
     * @throws Exception
     */
    private String sendAlarmList(LatestChatsResultDto latestChatsResultDto, WebSocketSession session) throws Exception {
        List<SendChatDto> alarmDtoList = latestChatsResultDto.getChats();
        double nextScore = latestChatsResultDto.getLastScore();

        log.info("메세지 갯수 조회 {}", alarmDtoList.size());

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(alarmDtoList)));

        Map<String, Object> data = new HashMap<>();
        data.put("cursor", Double.toString(nextScore));

        return objectMapper.writeValueAsString(data);
    }
}
