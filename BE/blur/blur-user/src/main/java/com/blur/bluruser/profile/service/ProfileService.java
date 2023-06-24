package com.blur.bluruser.profile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.blur.bluruser.match.entity.MatchMakingRating;
import com.blur.bluruser.match.entity.MatchSetting;
import com.blur.bluruser.match.repository.MatchMakingRatingRepository;
import com.blur.bluruser.match.repository.MatchSettingRepository;
import com.blur.bluruser.profile.dto.request.RequestProfileSettingDto;
import com.blur.bluruser.profile.dto.request.RequestUserInterestDto;
import com.blur.bluruser.profile.dto.response.ResponseCardDto;
import com.blur.bluruser.profile.dto.response.ResponseInterestDto;
import com.blur.bluruser.profile.dto.response.ResponseProfileSettingDto;
import com.blur.bluruser.profile.entity.Interest;
import com.blur.bluruser.profile.entity.UserInterest;
import com.blur.bluruser.profile.entity.UserProfile;
import com.blur.bluruser.profile.repository.InterestRepository;
import com.blur.bluruser.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;

    private final InterestRepository interestRepository;

    private final MatchSettingRepository matchSettingRepository;

    private final MatchMakingRatingRepository matchMakingRatingRepository;

    private final MongoTemplate mongoTemplate;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public Boolean check(String userId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile == null) {
            return false;
        } else if (userProfile.getAge() == null || userProfile.getGender() == null || userProfile.getNickname() == null) {
            return false;
        }
        return true;
    }

    public ResponseCardDto getCard(String userId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile == null) {
            userProfile = UserProfile.builder()
                    .userId(userId)
                    .build();
            userProfileRepository.save(userProfile);
            MatchSetting newSetting = MatchSetting.builder()
                    .userId(userId)
                    .userProfile(userProfile)
                    .build();
            matchSettingRepository.save(newSetting);
            MatchMakingRating mmr = MatchMakingRating.builder()
                    .userId(userId)
                    .userProfile(userProfile)
                    .build();
            matchMakingRatingRepository.save(mmr);
            UserInterest userInterest = UserInterest.builder()
                    .userId(userId)
                    .build();
            mongoTemplate.insert(userInterest, "user_interest");
        }
        UserInterest userInterest = mongoTemplate.findOne(
                Query.query(Criteria.where("userId").is(userId)),
                UserInterest.class
        );

        List<String> userInterests = userInterest.getInterests();
        ResponseCardDto responseCardDto = new ResponseCardDto(userProfile, userInterests); // 이부분 바꼈음 성훈님한테 말해야함
        //유저 관심사를 객체를 줬었는데 string으로 리팩토링
        return responseCardDto;
    }

    public ResponseProfileSettingDto getProfileSetting(String userId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        MatchSetting matchSetting = matchSettingRepository.findByUserId(userId);
        ResponseProfileSettingDto responseProfileSettingDto = new ResponseProfileSettingDto(userProfile, matchSetting);
        return responseProfileSettingDto;
    }

    public RequestProfileSettingDto updateProfile(String userId, RequestProfileSettingDto requestProfileSettingDto) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        userProfile.updateProfile(requestProfileSettingDto.getAge(), requestProfileSettingDto.getNickname(),
                requestProfileSettingDto.getGender(), requestProfileSettingDto.getIntroduce(), requestProfileSettingDto.getMbti());
        userProfileRepository.save(userProfile);
        return null;
    }

    public String updateImage(String userId, MultipartFile profileImage) throws IOException {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        String s3FileName = UUID.randomUUID() + "-" + profileImage.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(profileImage.getInputStream().available());
        amazonS3.putObject(bucket, s3FileName, profileImage.getInputStream(), objMeta);
        amazonS3.setObjectAcl(bucket, s3FileName, CannedAccessControlList.PublicRead);
        userProfile.updateImage(amazonS3.getUrl(bucket, s3FileName).toString());
        userProfileRepository.save(userProfile);
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public ResponseInterestDto getInterests(String userId) {

        List<Interest> allInterests = interestRepository.findAll();
        UserInterest userInterest = mongoTemplate.findOne(
                Query.query(Criteria.where("userId").is(userId)),
                UserInterest.class
        );

        List<String> userInterests = userInterest.getInterests();
        ResponseInterestDto responseInterestDto = new ResponseInterestDto(allInterests, userInterests);
        return responseInterestDto;
    }

    public void updateInterest(RequestUserInterestDto requestUserInterestDto, String userId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        UserInterest userInterest = mongoTemplate.findOne(
                Query.query(Criteria.where("userId").is(userId)),
                UserInterest.class
        );
        List<String> interests = requestUserInterestDto.getInterests();
        userInterest.update(interests);
        mongoTemplate.insert(userInterest, "user_interest");
    }
}
