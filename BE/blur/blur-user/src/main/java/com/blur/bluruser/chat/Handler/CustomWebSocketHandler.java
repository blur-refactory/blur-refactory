package com.blur.bluruser.chat.Handler;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;
import com.blur.bluruser.chat.dto.ReceiveDto;
import com.blur.bluruser.chat.dto.SendChatDto;
import com.blur.bluruser.chat.service.ChatMakeService;
import com.blur.bluruser.chat.service.ChatSendService;
import com.blur.bluruser.chat.utils.ChatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, Map<String, WebSocketSession>> userSessions = new HashMap<>();
    private final ChatMakeService chatMakeService;
    private final ChatSendService chatSendService;
    private final ChatUtils chatUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void addSession(String userId, String roomId, WebSocketSession session) {
        Map<String, WebSocketSession> roomSessions = userSessions.computeIfAbsent(userId, key -> new HashMap<>());
        roomSessions.put(roomId, session);
    }

//    public WebSocketSession getSession(String userId, String roomId) {
//        Map<String, WebSocketSession> roomSessions = userSessions.get(userId);
//        if (roomSessions != null) {
//            return roomSessions.get(roomId);
//        }
//        return null;
//    }

    public String getUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("id");
    }

    public String getRoomId(WebSocketSession session) {
        return (String) session.getAttributes().get("roomId");
    }

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
        log.info("연결한 채팅방 {}", roomId);

        session.sendMessage(new TextMessage(sendChatList(chatMakeService.chatDtoList(roomId, 0.0), session)));

        log.info("메세지 조회 성공");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String jsonPayload = message.getPayload().toString();
        ReceiveDto receivedMessage = objectMapper.readValue(jsonPayload, ReceiveDto.class);
        log.info("메세지 수신 시작");

        String userId = getUserId(session);
        String roomId = getRoomId(session);

        if (receivedMessage.getNickname() != null) {
            ChatDto chatDto = chatSendService.sendChat(userId, roomId, receivedMessage.getNickname(), receivedMessage.getMessage());
            sendMessageToUser(roomId, chatDto);
        }

        if (receivedMessage.getCursor() != null) {
            log.info("메세지 전송");
            session.sendMessage(new TextMessage(sendChatList(chatMakeService.chatDtoList(roomId, Double.parseDouble(receivedMessage.getCursor())), session)));
        }

        if (receivedMessage.getDeleteStart() != null && receivedMessage.getDeleteEnd() != null) {
            log.info("메세지 삭제 시도");
            boolean deleteResult = chatMakeService.deleteChat(roomId, Double.parseDouble(receivedMessage.getDeleteStart()), Double.parseDouble(receivedMessage.getDeleteEnd()));
            if (deleteResult) {
                log.info("메세지 삭제 성공");
            } else {
                log.info("메세지 삭제 실패");
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        String roomId = getRoomId(session);

        removeSession(userId, roomId);
        log.info("유저 로그아웃 {}", userId, roomId);
        log.info("소켓 연결 종료 {}", status);
        session.close();
    }

    /**
     * 이벤트 발생 시 알람 전송
     *
     * @param roomId
     * @param chatDto
     * @throws Exception
     */
    public void sendMessageToUser(String roomId, ChatDto chatDto) throws Exception {
        for (Map<String, WebSocketSession> roomSessions : userSessions.values()) {
            WebSocketSession session = roomSessions.get(roomId);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                                new SendChatDto(chatDto.getUserId(),
                                        chatDto.getNickname(),
                                        chatDto.getMessage(),
                                        chatDto.getFormattedCreatedAt(),
                                        chatUtils.changeLocalDateTimeToDouble(chatDto.getCreatedAt())))
                        )
                );
            } else {
                log.warn("수신자의 세션이 닫혀있거나 존재하지 않음 : {}", roomId);
            }
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
    private String sendChatList(LatestChatsResultDto latestChatsResultDto, WebSocketSession session) throws Exception {
        List<SendChatDto> alarmDtoList = latestChatsResultDto.getChats();
        double nextScore = latestChatsResultDto.getLastScore();

        log.info("메세지 갯수 조회 {}", alarmDtoList.size());

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(alarmDtoList)));

        Map<String, Object> data = new HashMap<>();
        data.put("cursor", Double.toString(nextScore));

        return objectMapper.writeValueAsString(data);
    }
}
