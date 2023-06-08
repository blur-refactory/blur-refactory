package com.blur.bluruser.match.service;

import com.blur.bluruser.match.dto.request.RequestAcceptDto;
import com.blur.bluruser.match.dto.request.RequestFemaleCheckDto;
import com.blur.bluruser.match.dto.request.RequestMatchDto;
import com.blur.bluruser.match.dto.request.RequestUpdateSettingDto;
import com.blur.bluruser.match.dto.response.ResponseAceeptDto;
import com.blur.bluruser.match.dto.response.ResponseCheckDto;
import com.blur.bluruser.match.dto.response.ResponseMatchSettingDto;
import com.blur.bluruser.match.dto.response.ResponseStartDto;
import com.blur.bluruser.match.entity.MatchMakingRating;
import com.blur.bluruser.match.entity.MatchSetting;
import com.blur.bluruser.match.dto.*;
import com.blur.bluruser.match.entity.MatchedUser;
import com.blur.bluruser.match.repository.MatchMakingRatingRepository;
import com.blur.bluruser.match.repository.MatchSettingRepository;
import com.blur.bluruser.match.repository.MatchedUserRepository;
import com.blur.bluruser.profile.entity.UserInterest;
import com.blur.bluruser.profile.entity.UserProfile;
import com.blur.bluruser.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchSettingRepository matchSettingRepository;

    private final MatchMakingRatingRepository matchMakingRatingRepository;

    private final UserProfileRepository userProfileRepository;

    private final MatchedUserRepository matchedUserRepository;

    private static Map<String, MatchDto> males = new ConcurrentHashMap<>();

    private static Map<String, MatchDto> females = new ConcurrentHashMap<>();

    private static Map<String, List<String>> success = new ConcurrentHashMap<>();

    public ResponseMatchSettingDto getSetting(String userId) {

        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        System.out.println(matchSetting == null);
        if (matchSetting == null) {
            matchSetting = MatchSetting.builder()
                    .userId(userId)
                    .build();
            matchSettingRepository.save(matchSetting);
        };
        ResponseMatchSettingDto responseMatchSettingDto = new ModelMapper().map(matchSetting, ResponseMatchSettingDto.class);
        return responseMatchSettingDto;
    }

    public void updateSetting(String userId, RequestUpdateSettingDto requestUpdateSettingDto) {

        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        matchSetting.update(requestUpdateSettingDto);
        matchSettingRepository.save(matchSetting);
    }

    public ResponseStartDto matchStart(String userId, RequestMatchDto requestMatchDto) {

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
        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        MatchDto matchDto = new MatchDto(requestMatchDto, matchSetting, matchMakingRating, userProfile);
        ResponseStartDto responseStartDto = new ResponseStartDto();
        String gender = matchDto.getGender();
        responseStartDto.setGender(gender);
        if (matchDto.getGender().equals("M")) {
            males.put(matchDto.getUserId(), matchDto);
        }
        else {
            females.put(matchDto.getUserId(), matchDto);
        }
        return responseStartDto;
    }

    public ResponseCheckDto femaleCheck(String userId, RequestFemaleCheckDto requestFemaleCheckDto) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        Queue<QueueDto> maleList = new PriorityQueue<>((o1, o2) -> {
            if(o1.getPoint()  == o2.getPoint()) {
                return o2.getPoint();
            }
            return Integer.compare(o2.getPoint(), o1.getPoint());
        });
        MatchDto femaleDto = females.get(userId);

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
            ResponseCheckDto responseCheckDto = new ResponseCheckDto(selectedMale);
            return responseCheckDto;
        }
        ResponseCheckDto responseMatchDto = new ResponseCheckDto();
        return responseMatchDto;
    }

    public ResponseCheckDto maleCheck(String userId) {

        List<String> successInfo = success.get(userId);
        ResponseCheckDto responseCheckDto = new ResponseCheckDto();
        if(successInfo == null) {

            return responseCheckDto;
        }
        String sessionId = successInfo.get(0);
        String partnerId = successInfo.get(1);
        responseCheckDto.setSessionId(sessionId);
        responseCheckDto.setPartnerId(partnerId);
        return  responseCheckDto;
    }

    public void matchDecline(String userId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile.getGender() == "M") {
            males.remove(userId);

        }
        females.remove(userId);
        success.remove(userId);
    }

    public ResponseAceeptDto matchAccept(String userId, RequestAcceptDto requestAcceptDto) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile.getGender().equals("F")) {
            MatchDto selectedMale = males.get(requestAcceptDto.getPartnerId());
            if (selectedMale == null) {
                return null;
            }
            males.remove(selectedMale.getUserId());
            List<String> successInfo = new ArrayList<>();
            successInfo.add(requestAcceptDto.getSessionId());
            successInfo.add(userId);
            success.put(requestAcceptDto.getPartnerId(), successInfo);
            String partnerId = requestAcceptDto.getPartnerId();
            UserProfile partner = userProfileRepository.findByUserId(partnerId);
            List<String> partnerInterests = new ArrayList<>();
            for (UserInterest partnerInterest: partner.getUserInterests()) {
                partnerInterests.add(partnerInterest.getInterest().getInterestName());
            }
            ResponseAceeptDto responseAceeptDto = new ResponseAceeptDto(partner, partnerInterests);
            return responseAceeptDto;
        }
        else {
            List<String> successInfo = success.get(userId);
            if (successInfo == null) {
                return null;
            }
            String sessionId = successInfo.get(0);
            String partnerId = successInfo.get(1);
            success.remove(userId);
            UserProfile partner = userProfileRepository.findByUserId(partnerId);
            List<String> partnerInterests = new ArrayList<>();
            for (UserInterest partnerInterest: partner.getUserInterests()) {
                partnerInterests.add(partnerInterest.getInterest().getInterestName());
            }
            ResponseAceeptDto responseAceeptDto = new ResponseAceeptDto(partner, partnerInterests);
            return responseAceeptDto;
        }
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
