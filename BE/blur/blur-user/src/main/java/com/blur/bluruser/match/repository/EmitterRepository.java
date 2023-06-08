package com.blur.bluruser.match.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class EmitterRepository {

    // 유저ID를 키로 SseEmitter를 해시맵에 저장할 수 있도록 구현했다.
    private static Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter save(String username, SseEmitter sseEmitter) {

        String key = getKey(username);

        if (!emitterMap.containsKey(key)) {
            emitterMap.put(key, sseEmitter);
            log.info("Saved SseEmitter for {}", username);
        } else {
            log.info("SseEmitter for {} already exists", username);
            SseEmitter existingEmitter = emitterMap.get(key);
            existingEmitter.complete(); // 기존 값 제거
            emitterMap.put(key, sseEmitter);
            log.info("SseEmitter for {} already exists", username);
        }
        log.info("구독구독구독");
        log.info("Saved SseEmitter for {}", username);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(String username) {
        log.info("Got SseEmitter for {}", username);
        return Optional.ofNullable(emitterMap.get(getKey(username)));
    }

    public void delete(String username) {
        emitterMap.remove(getKey(username));
        log.info("Deleted SseEmitter for {}", username);
    }

    private String getKey(String username) {
        return "Emitter:UID:" + username;
    }

    public Boolean contains(String username) {
        String key = getKey(username);
        if (emitterMap.containsKey(key)) {
            return true;
        }
        return false;
    }
}


