package com.blur.bluruser.chat.utils;


import com.blur.bluruser.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Log4j2
public class RedisSchedule {
    private final RedisTemplate redisTemplate;
    private final ChatUtils chatUtils;
    private final RedisTemplate<String, ChatDto> alarmRedisTemplate;
    private ZSetOperations<String, ChatDto> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = alarmRedisTemplate.opsForZSet();
    }

    // 매일 자정에 실행
//    @Transactional
//    @Scheduled(cron = "0 0 0 * * *")
//    public void deleteChangeLikeFromRedis() {
//        log.info("[Scheduling] redis like caching start");
//
//        Set<String> redisChangeKeys = redisTemplate.keys("change*");
//
//        if (redisChangeKeys == null) {
//            return;
//        }
//
//        Iterator<String> changeKeys = redisChangeKeys.iterator();
//
//        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
//
//        while (changeKeys.hasNext()) {
//
//            String changeKey = changeKeys.next();
//            int titleId = Integer.parseInt(changeKey.split("::")[1]);
//
//            Set<String> membersChangeInRedis = setOperations.members(changeKey);
//
//            for (String memberId : membersChangeInRedis) {
//                Likes likes = mandalartLikeService.checkLike(memberId, titleId);
//
//                // DB 에 없으면 like 생성 후 DB 저장
//                if (likes == null) {
//                    Title title = mandalartLikeService.checkTitle(titleId);
//                    Member member = mandalartLikeService.checkMember(memberId);
//
//                    Likes newLike = Likes.builder()
//                            .title(title)
//                            .status(true)
//                            .member(member)
//                            .build();
//                    likeRepository.save(newLike);
//                    log.info("Like DB Save");
//                } else {    // DB 있으면
//                    // status 1로 변경 아니면 0 변경
//                    likes.setStatus(!likes.isStatus());
//                }
//                // 검색을 위한 title 테이블의 like_cnt update
//                updateLikeCnt(titleId);
//            }
//            // 변경 redis caching 데이터 삭제
//            log.info("[Scheduling] 좋아요 변경 redis caching 데이터 삭제 ");
//            redisTemplate.delete(changeKey);
//        }
//    }

//    @Scheduled(cron = "0 0 0 * * *")
//    public void cleanupExpiredAlarms() {
//        log.info("[Scheduling] 알람 데이터 삭제 시작");
//
//        LocalDateTime expirationDateTime = LocalDateTime.now().minusHours(24);
//
//        double expirationTimestamp = chatUtils.changeLocalDateTimeToDouble(expirationDateTime);
//
//        // Redis에서 expirationTime보다 작은 스코어를 가지는 데이터 삭제
//        Set<String> allKeys = alarmRedisTemplate.keys("*"); // 모든 키 가져오기
//        for (String key : allKeys) {
//            Set<ChatDto> expiredAlarms = zSetOperations.rangeByScore(key, 0, expirationTimestamp);
//            for (ChatDto alarmDto : expiredAlarms) {
//                zSetOperations.remove(key, alarmDto);
//            }
//        }
//        log.info("[Scheduling] 알람 데이터 삭제 완료");
//    }

    // 일주일에 한 번 실행
//    @Scheduled(cron = "0 0 0 * * 0")
//    public void deleteLikeFromRedis() {
//        // 기존 redis caching 데이터 삭제
//        log.info("[Scheduling] 좋아요 기존 redis caching 데이터 삭제 ");
//        Set<String> redisLikeKeys = redisTemplate.keys("like*");
//        if (redisLikeKeys == null) {
//            return;
//        }
//        Iterator<String> likeKeys = redisLikeKeys.iterator();
//
//        while (likeKeys.hasNext()) {
//            String likeKey = likeKeys.next();
//            redisTemplate.delete(likeKey);
//        }
//    }

//    @Transactional
//    public void updateLikeCnt(int titleId) {
//        Title findTitle = mandalartLikeService.checkTitle(titleId);
//        // 변경된 title id 만 db 개수 조회해서 update
//        int likeCnt = likeRepository.countByTitleIdAndStatus(titleId, true).intValue();
//        findTitle.setLikeCnt(likeCnt);
//        mandalartService.saveClearTitle(findTitle);
//    }

}
