package com.blur.bluruser.match.service;

import com.blur.bluruser.match.dto.response.ResponseAlarmDto;
import com.blur.bluruser.match.repository.EmitterRepository;
import com.blur.bluruser.profile.entity.UserProfile;
import com.blur.bluruser.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserProfileRepository userProfileRepository;

    private final EmitterRepository emitterRepository;

    private final static Long DEFAULT_TIMEOUT = 3600000L;

    private final static String NOTIFICATION_NAME = "notify";


    public SseEmitter subscribe(String userId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 유저 ID로 SseEmitter를 저장한다.
        emitterRepository.save(userId, sseEmitter);

        // 세션이 종료될 경우 저장한 SseEmitter를 삭제한다.
        // 세션이 종료될 경우 예외 처리를 한다.
        sseEmitter.onTimeout(() -> {
            if (emitterRepository.contains(userId)) {
                System.out.println("타임아웃 삭제ㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔㅔ");
                emitterRepository.delete(userId);
            }
        });

        sseEmitter.onCompletion(() -> {
            if (emitterRepository.contains(userId)) {
                System.out.println("onCompletion 삭제ㅔㅔㅔㅔ[ㅔㅔㅔㅔㅔ");
                emitterRepository.delete(userId);
            }
        });


        // 503 Service Unavailable 오류가 발생하지 않도록 첫 데이터를 보낸다.
        try {
            sseEmitter.send(SseEmitter.event().id(userId).name(NOTIFICATION_NAME).data("Connection completed"));
        } catch (IOException exception) {
            throw new RuntimeException("Failed to send SSE event", exception);
        }
        return sseEmitter;
    }

    public void send(String username, Long alarmId, ResponseAlarmDto responseAlarmDto) {
        // 유저 ID로 SseEmitter를 찾아 이벤트를 발생 시킨다.
        emitterRepository.get(username).ifPresentOrElse(sseEmitter -> {
            try {
                System.out.println("보내기보내기보내기");
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(NOTIFICATION_NAME).data(responseAlarmDto));
            } catch (IOException exception) {
                // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                System.out.println(exception);
                emitterRepository.delete(username);
            }
        }, () -> System.out.println("No Emitter found"));
    }

}
