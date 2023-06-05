package com.blur.blurmatch.service;

import com.blur.blurmatch.dto.*;
import com.blur.blurmatch.dto.request.RequestAcceptDto;
import com.blur.blurmatch.dto.request.RequestMatchDto;
import com.blur.blurmatch.dto.response.ResponseAceeptDto;
import com.blur.blurmatch.dto.response.ResponseMatchDto;
import com.blur.blurmatch.dto.response.ResponseProfileDto;
import com.blur.blurmatch.entity.MatchMakingRating;
import com.blur.blurmatch.entity.MatchSetting;
import com.blur.blurmatch.entity.MatchedUser;
import com.blur.blurmatch.entity.Report;
import com.blur.blurmatch.repository.MatchMakingRatingRepository;
import com.blur.blurmatch.repository.MatchSettingRepository;
import com.blur.blurmatch.repository.MatchedUserRepository;
import com.blur.blurmatch.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MatchService {

    @Autowired
    private MatchSettingRepository matchSettingRepository;

    @Autowired
    private MatchMakingRatingRepository matchMakingRatingRepository;

    @Autowired
    private MatchedUserRepository matchedUserRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    private static Map<String, MatchDto> males = new ConcurrentHashMap<>();

    private static Map<String, List<String>> success = new ConcurrentHashMap<>();

    public MatchSettingDto getSetting(String userId) {

        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        System.out.println(matchSetting == null);
        if (matchSetting == null) {
            matchSetting = MatchSetting.builder()
                    .userId(userId)
                    .build();
            matchSettingRepository.save(matchSetting);
        };
        MatchSettingDto matchSettingDto = new ModelMapper().map(matchSetting, MatchSettingDto.class);
        return matchSettingDto;
    }

    public void createSetting(MatchSettingDto matchSettingDto) {

        MatchSetting matchSetting = new ModelMapper().map(matchSettingDto, MatchSetting.class);
        matchSettingRepository.save(matchSetting);
    }

    public void updateSetting(MatchSettingDto matchSettingDto) {

        String userId = matchSettingDto.getUserId();
        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        matchSetting.update(matchSettingDto);
        matchSettingRepository.save(matchSetting);
    }

    public Integer settingCheck(String userId) {

        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        if (matchSetting == null) {
            return 0;
        } else if (matchSetting.getMinAge() == null || matchSetting.getMaxAge() == null || matchSetting.getMaxDistance() == null) {
            return 1;
        }
        return 2;
    }

    public ResponseMatchDto matchStart(RequestMatchDto requestMatchDto) {

        System.out.println(males.toString());
        String userId = requestMatchDto.getUserId();
        MatchMakingRating matchMakingRating = matchMakingRatingRepository.findByUserId(userId);
        if (matchMakingRating == null) {
            matchMakingRating = MatchMakingRating.builder()
                    .userId(userId)
                    .point(1000)
                    .winningStreak(0)
                    .losingStreak(0)
                    .reportCount(0)
                    .build();
            matchMakingRatingRepository.save(matchMakingRating);
        }
        if (matchMakingRating.getReportCount() > 10) {
            return null;
        }
        String getProfileUrl = String.format(env.getProperty("blur-profile.url")) + "/" + userId + "/service";
        ResponseEntity<ResponseProfileDto> profileResponse = restTemplate.getForEntity(getProfileUrl, ResponseProfileDto.class, userId);
        ResponseProfileDto responseProfileDto = profileResponse.getBody();
        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        MatchDto matchDto = new MatchDto(requestMatchDto, matchSetting, matchMakingRating, responseProfileDto);
        if (matchDto.getGender().equals("M")) {
            males.put(matchDto.getUserId(), matchDto);
            ResponseMatchDto responseMatchDto = new ResponseMatchDto();
            responseMatchDto.setMyGender(matchDto.getGender());
            return responseMatchDto;
        }
        Queue<QueueDto> maleList = new PriorityQueue<>((o1, o2) -> {
            if(o1.getPoint()  == o2.getPoint()) {
                return o2.getPoint();
            }
            return Integer.compare(o2.getPoint(), o1.getPoint());
        });
        MatchDto femaleDto = matchDto;

        for (String male : males.keySet()) {
            MatchDto maleDto = males.get(male);
            if (!filter(maleDto, femaleDto)) {continue;}
            QueueDto maleQueue = new QueueDto(maleDto);
            maleList.offer(maleQueue);
        }

        while (!maleList.isEmpty()) {
            String maleId = maleList.poll().getUserId();
            MatchDto selectedMale = males.get(maleId);
            if (selectedMale == null) {continue;}
            ResponseMatchDto responseMatchDto = new ResponseMatchDto(femaleDto, selectedMale);
            return responseMatchDto;
        }
        ResponseMatchDto responseMatchDto = new ResponseMatchDto();
        responseMatchDto.setMyGender(femaleDto.getGender());
        return responseMatchDto;
    }

    public ResponseMatchDto matchCheck(String userId, String gender) {
        List<String> successInfo = success.get(userId);
        ResponseMatchDto responseMatchDto = new ResponseMatchDto();
        responseMatchDto.setMyGender(gender);
        if(successInfo == null) {
            return responseMatchDto;
        }
        String sessionId = successInfo.get(0);
        String partnerId = successInfo.get(1);
        responseMatchDto.setSessionId(sessionId);
        responseMatchDto.setPartnerId(partnerId);
        return  responseMatchDto;
    }

    public ResponseAceeptDto matchAccept(RequestAcceptDto requestAcceptDto) {

        if (requestAcceptDto.getMyGender().equals("F")) {
            MatchDto selectedMale = males.get(requestAcceptDto.getPartnerId());
            if (selectedMale == null) {
                return null;
            }
            males.remove(selectedMale.getUserId());
            List<String> successInfo = new ArrayList<>();
            successInfo.add(requestAcceptDto.getSessionId());
            successInfo.add(requestAcceptDto.getUserId());
            success.put(requestAcceptDto.getPartnerId(), successInfo);
            String partnerId = requestAcceptDto.getPartnerId();
            String getUserInterestUrl = String.format(env.getProperty("blur-profile.url")) + "/" + partnerId + "/service/partner";
            ResponseEntity<Collection<String>> partnerInterests = restTemplate.exchange(
                    getUserInterestUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<String>>(){}
            );
            Collection<String> partnerInterestsBody = partnerInterests.getBody();
            String getProfileUrl = String.format(env.getProperty("blur-profile.url")) + "/" + partnerId + "/service";
            ResponseEntity<ResponseProfileDto> profileResponse = restTemplate.getForEntity(getProfileUrl , ResponseProfileDto.class, partnerId);
            String partnerNickname = profileResponse.getBody().getNickname();
            ResponseAceeptDto responseAceeptDto = new ResponseAceeptDto(requestAcceptDto, partnerNickname, partnerInterestsBody);
            return responseAceeptDto;
        }
        else {
            List<String> successInfo = success.get(requestAcceptDto.getUserId());
            if (successInfo == null) {
                return null;
            }
            String sessionId = successInfo.get(0);
            String partnerId = successInfo.get(1);
            success.remove(requestAcceptDto.getUserId());
            String getUserInterestUrl = String.format(env.getProperty("blur-profile.url")) + "/" + partnerId + "/service/partner";
            ResponseEntity<Collection<String>> partnerInterests = restTemplate.exchange(
                    getUserInterestUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<String>>(){}
            );
            Collection<String> partnerInterestsBody = partnerInterests.getBody();
            String getProfileUrl = String.format(env.getProperty("blur-profile.url")) + "/" + partnerId + "/service";
            ResponseEntity<ResponseProfileDto> profileResponse = restTemplate.getForEntity(getProfileUrl , ResponseProfileDto.class, partnerId);
            String partnerNickname = profileResponse.getBody().getNickname();
            ResponseAceeptDto responseAceeptDto = new ResponseAceeptDto(partnerId, partnerNickname, partnerInterestsBody, sessionId);
            return responseAceeptDto;
        }
    }

    public void matchDecline(String userId) {

        males.remove(userId);
        success.remove(userId);
    }

    public String meetingExit(MeetingDto meetingDto) {

        String userId = meetingDto.getUserId();
        String partnerId = meetingDto.getPartnerId();
        MatchedUser userMet = matchedUserRepository.findByUserId(userId);
        MatchedUser partnerMet = matchedUserRepository.findByUserId(partnerId);
        if (userMet == null) {
            userMet = MatchedUser.builder()
                    .userId(userId)
                    .build();
            matchedUserRepository.save(userMet);
        }
        if (partnerMet == null) {
            partnerMet = MatchedUser.builder()
                    .userId(partnerId)
                    .build();
            matchedUserRepository.save(partnerMet);
        }
        Collection<String> userMetList = userMet.getMatchedList();
        Collection<String> partnerMetList = partnerMet.getMatchedList();
        userMetList.add(partnerId);
        partnerMetList.add(userId);
        userMet.update(userMetList);
        partnerMet.update(partnerMetList);
        matchedUserRepository.save(userMet);
        matchedUserRepository.save(partnerMet);
        Integer playTime = meetingDto.getPlayTime();
        MatchMakingRating myMmr = matchMakingRatingRepository.findByUserId(userId);
        MatchMakingRating partnerMmr = matchMakingRatingRepository.findByUserId(partnerId);
        String res = mmrUpdate(myMmr, partnerMmr, playTime);
        return res;
    }

    public void meetingReport(ReportDto reportDto) {

        Report report = new ModelMapper().map(reportDto, Report.class);
        MatchMakingRating reportedUser = matchMakingRatingRepository.findByUserId(reportDto.getReportedUserId());
        Integer reportCount = reportedUser.getReportCount();
        reportCount += 1;
        reportedUser.report(reportCount);
        matchMakingRatingRepository.save(reportedUser);
        reportRepository.save(report);
    }

    private boolean filter(MatchDto maleDto, MatchDto femaleDto) {

        MatchedUser matchedUsers = matchedUserRepository.findByUserId(femaleDto.getUserId());
        if (matchedUsers == null) {
            matchedUsers = MatchedUser.builder()
                    .userId(femaleDto.getUserId())
                    .build();
            matchedUserRepository.save(matchedUsers);
        }
        Collection<String> matchedList = matchedUsers.getMatchedList();
        if (matchedList != null) {
            if (matchedList.contains(maleDto.getUserId())) {return false;}
        }
        int maleAge = maleDto.getAge();
        int femaleAge = femaleDto.getAge();
        if (femaleAge < maleDto.getMinAge() || femaleAge > maleDto.getMaxAge() || maleAge < femaleDto.getMinAge() || maleAge > femaleDto.getMaxAge()) {return false;}
        double lat1 = maleDto.getLat();
        double lng1 = maleDto.getLng();
        double lat2 = femaleDto.getLat();
        double lng2 = femaleDto.getLng();
        double distance = distance(lat1, lng1, lat2, lng2);
        if (maleDto.getMaxDistance() < distance || femaleDto.getMaxDistance() < distance) {return false;}
        return true;

    }

    private static double distance(double lat1, double lng1, double lat2, double lng2) {

        double theta = lng1 - lng2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private String mmrUpdate(MatchMakingRating myMmr, MatchMakingRating partnerMmr, Integer playTime) {

        Integer partnerPoint = partnerMmr.getPoint();
        if (playTime < 200) {
            Integer partnerLosing = partnerMmr.getLosingStreak();
            if (playTime < 50) {
                partnerPoint -= (10 + partnerLosing);
                partnerLosing += 1;
            } else if (playTime < 100) {
                partnerPoint -= (7 + partnerLosing);
                partnerLosing += 1;
            } else if (playTime < 150) {
                partnerPoint -= (4 + partnerLosing);
                partnerLosing += 1;
            } else {
                partnerPoint -= (1 + partnerLosing);
                partnerLosing += 1;
            }
            partnerMmr.update(partnerPoint, 0, partnerLosing);
            matchMakingRatingRepository.save(partnerMmr);
            return "Success";
        }
        Integer myPoint = myMmr.getPoint();
        Integer myWinning = myMmr.getWinningStreak();
        Integer partnerWinning = partnerMmr.getWinningStreak();
        if (playTime < 300) {
            partnerPoint += (1 + partnerWinning);
            myPoint += (1 + myWinning);
            partnerWinning += 1;
            myWinning += 1;
        } else if (playTime < 400) {
            partnerPoint += (4 + partnerWinning);
            myPoint += (4 + myWinning);
            partnerWinning += 1;
            myWinning += 1;
        } else if (playTime < 500) {
            partnerPoint += (7 + partnerWinning);
            myPoint += (7 + myWinning);
            partnerWinning += 1;
            myWinning += 1;
        } else if (playTime < 600) {
            partnerPoint += (10 + partnerWinning);
            myPoint += (10 + myWinning);
            partnerWinning += 1;
            myWinning += 1;
        } else {
            partnerPoint += (13 + partnerWinning);
            myPoint += (13 + myWinning);
            partnerWinning += 1;
            myWinning += 1;
        }
        partnerMmr.update(partnerPoint, partnerWinning, 0);
        matchMakingRatingRepository.save(partnerMmr);
        myMmr.update(myPoint, myWinning, 0);
        matchMakingRatingRepository.save(myMmr);
        return "Success";
    }
}
