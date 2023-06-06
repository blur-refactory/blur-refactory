package com.blur.bluruser.chat.repository;

import com.blur.bluruser.chat.dto.ChatDto;
import com.blur.bluruser.chat.dto.LatestChatsResultDto;
import com.blur.bluruser.chat.dto.SendChatDto;
import com.blur.bluruser.chat.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisChatRepositoryImpl implements RedisChatRepository {
    private final RedisTemplate<String, ChatDto> alarmRedisTemplate;
    private final ChatUtils chatUtils;
    private ZSetOperations<String, ChatDto> zSetOperations;
    private static final double NO_MORE_ALARMS = -1.0;
    private static final int MAX_ALARM_COUNT = 20;

    @PostConstruct
    private void init() {
        zSetOperations = alarmRedisTemplate.opsForZSet();
    }

    /**
     * 알람을 저장합니다. 저장 시간을 score로 사용해서 정렬
     *
     * @param chatDto 저장할 알람 객체
     */
    @Override
    public void save(ChatDto chatDto) {
        zSetOperations.add(chatDto.getRoomId(),
                chatDto,
                chatUtils.changeLocalDateTimeToDouble(chatDto.getCreatedAt()));
    }

    /**
     * 최신 알람을 조회합니다.
     *
     * @param roomId    알람을 조회할 채팅방 ID
     * @param lastScore 이전 조회의 마지막 점수
     * @return 최신 알람과 마지막 점수를 담은 결과 맵
     */
    @Override
    public LatestChatsResultDto getLatestChats(String roomId, double lastScore) {
        LatestChatsResultDto latestChatsResultDto = new LatestChatsResultDto();

        if (lastScore == 0) {
            // 처음부터 조회하는 경우
            Set<ZSetOperations.TypedTuple<ChatDto>> chats = zSetOperations.reverseRangeByScoreWithScores(roomId, 0, Double.POSITIVE_INFINITY, 0, MAX_ALARM_COUNT);
            if (chats == null || chats.isEmpty()) {
                return sendLastPoint(latestChatsResultDto);
            } else {
                processAlarms(chats, latestChatsResultDto);
            }

        } else if (lastScore > 0) {
            // 이전 조회의 시작점을 기준으로 이어서 조회하는 경우
            Set<ZSetOperations.TypedTuple<ChatDto>> alarms = zSetOperations.reverseRangeByScoreWithScores(roomId, 0, lastScore, 0, MAX_ALARM_COUNT);
            if (alarms == null || alarms.isEmpty()) {
                return sendLastPoint(latestChatsResultDto);
            } else {
                processAlarms(alarms, latestChatsResultDto);
            }
        } else {
            // 잘못된 lastScore 값이 전달된 경우
            return sendLastPoint(latestChatsResultDto);
        }

        return latestChatsResultDto;
    }

    @Override
    public boolean delete(String roomId, double deleteStart, double deleteEnd) {
        log.info("초기 시작값, 종료값 {}, {}", deleteStart, deleteEnd);
        if (deleteStart > deleteEnd) {
            double tmp = deleteStart;
            deleteStart = deleteEnd;
            deleteEnd = tmp;
        }
        log.info("변경된 시작값, 종료값 {}, {}", deleteStart, deleteEnd);

        Set<ChatDto> valuesToDelete = zSetOperations.rangeByScore(roomId, deleteStart, deleteEnd);

        if (valuesToDelete == null || valuesToDelete.isEmpty())
            return false;

        Long removedCount = zSetOperations.remove(roomId, valuesToDelete.toArray());
        return removedCount != null && removedCount > 0;
    }

    /**
     * getLatestAlarms의 실행 결과로 나온 알람들을 처리하고 결과를 맵에 저장합니다.
     *
     * @param chats                처리할 알람 목록
     * @param latestChatsResultDto 처리 결과를 저장할 LatestAlarmsResultDto
     */
    private void processAlarms(Set<ZSetOperations.TypedTuple<ChatDto>> chats, LatestChatsResultDto latestChatsResultDto) {
        List<SendChatDto> alarmList = new ArrayList<>();
        double minScore = Double.MAX_VALUE;

        for (ZSetOperations.TypedTuple<ChatDto> tuple : chats) {
            double score = tuple.getScore();
            ChatDto chatDto = tuple.getValue();
            if (chatDto != null) {
                alarmList.add(new SendChatDto(chatDto.getUserId(),
                        chatDto.getNickname(),
                        chatDto.getMessage(),
                        chatDto.getFormattedCreatedAt(),
                        score));
                if (score < minScore) {
                    minScore = score;
                }
            }
        }

        latestChatsResultDto.setChats(alarmList);

        if (alarmList.size() < MAX_ALARM_COUNT) {
            latestChatsResultDto.setLastScore(NO_MORE_ALARMS);
        } else {
            latestChatsResultDto.setLastScore(minScore);
        }
    }

    private LatestChatsResultDto sendLastPoint(LatestChatsResultDto latestChatsResultDto) {
        latestChatsResultDto.setChats(Collections.emptyList());
        latestChatsResultDto.setLastScore(NO_MORE_ALARMS);
        return latestChatsResultDto;
    }
}
