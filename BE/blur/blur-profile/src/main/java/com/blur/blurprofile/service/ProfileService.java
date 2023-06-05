package com.blur.blurprofile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.MultipartUpload;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.blur.blurprofile.dto.*;
import com.blur.blurprofile.dto.request.RequestProfileSettingDto;
import com.blur.blurprofile.dto.request.RequestUserInterestDto;
import com.blur.blurprofile.dto.response.ResponseCardDto;
import com.blur.blurprofile.dto.response.ResponseInterestDto;
import com.blur.blurprofile.dto.response.ResponseProfileSettingDto;
import com.blur.blurprofile.entity.Interest;
import com.blur.blurprofile.entity.UserInterest;
import com.blur.blurprofile.entity.UserProfile;
import com.blur.blurprofile.repository.InterestRepository;
import com.blur.blurprofile.repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.blur.blurprofile.repository.UserProfileRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserInterestRepository userInterestRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3 amazonS3;

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
        }
        List<UserInterest> UserInterests = userInterestRepository.findByUserProfile(userProfile);
        List<Interest> userInterests = new ArrayList<>();
        for (UserInterest UserInterest : UserInterests) {
            userInterests.add(UserInterest.getInterest());
        }
        ResponseCardDto responseCardDto = new ResponseCardDto(userProfile, userInterests);
        return responseCardDto;
    }

    public ResponseProfileSettingDto getProfileSetting(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile == null) {
            userProfile = UserProfile.builder()
                    .userId(userId)
                    .build();
            userProfileRepository.save(userProfile);
        }
        ProfileDto profileDto = new ModelMapper().map(userProfile, ProfileDto.class);
        String getMatchSettingUrl = String.format(env.getProperty("blur-match.url")) + "/getSetting?userId=" + userId;
        ResponseEntity<MatchSettingDto> response = restTemplate.getForEntity(getMatchSettingUrl, MatchSettingDto.class);
        MatchSettingDto matchSetting = response.getBody();
        String getUserEmailUrl = String.format(env.getProperty("blur-auth.url")) + "/getEmail?userId=" + userId;
        ResponseEntity<String> email = restTemplate.getForEntity(getUserEmailUrl, String.class);
        String userEmail = email.getBody();
        ResponseProfileSettingDto responseProfileSettingDto = new ResponseProfileSettingDto(profileDto, matchSetting, userEmail);
        return responseProfileSettingDto;
    }

    public RequestProfileSettingDto updateProfile(RequestProfileSettingDto requestProfileSettingDto) {
        String userId = requestProfileSettingDto.getUserId();
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        userProfile.updateProfile(requestProfileSettingDto.getAge(), requestProfileSettingDto.getNickname(),
                requestProfileSettingDto.getGender(), requestProfileSettingDto.getIntroduce(), requestProfileSettingDto.getMbti());
        userProfileRepository.save(userProfile);
        MatchSettingDto matchSettingDto = new ModelMapper().map(requestProfileSettingDto, MatchSettingDto.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MatchSettingDto> request = new HttpEntity<>(matchSettingDto, headers);
        String updateMatchSettingUrl = String.format(env.getProperty("blur-match.url")) + "/updateSetting";
        System.out.println(updateMatchSettingUrl);
        ResponseEntity<String> response = restTemplate.exchange(updateMatchSettingUrl, HttpMethod.PUT, request, String.class);
        return requestProfileSettingDto;
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
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        List<Interest> allInterests = interestRepository.findAll();
        List<UserInterest> UserInterests = userInterestRepository.findByUserProfile(userProfile);
        List<Interest> userInterests = new ArrayList<>();
        for (UserInterest userInterest : UserInterests) {
            userInterests.add(userInterest.getInterest());
        }
        ResponseInterestDto responseInterestDto = new ResponseInterestDto(allInterests, userInterests);
        return responseInterestDto;
    }

    public void updateInterest(RequestUserInterestDto requestUserInterestDto, String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        List<UserInterest> userInterests = userInterestRepository.findByUserProfile(userProfile);
        userInterestRepository.deleteAll(userInterests);
        List<String> interests = requestUserInterestDto.getInterests();
        for (String interestName : interests) {
            Interest interest = interestRepository.findByInterestName(interestName);
            UserInterest userInterest = UserInterest.builder()
                    .userProfile(userProfile)
                    .interest(interest)
                    .build();
            userInterestRepository.save(userInterest);
        }
    }

    public ProfileDto getProfile(String userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile == null) {
            userProfile = UserProfile.builder()
                    .userId(userId)
                    .build();
            userProfileRepository.save(userProfile);
        }
        ProfileDto profileDto = new ModelMapper().map(userProfile, ProfileDto.class);
        return profileDto;
    }

    public Collection<String> getPartnerInterest(String partnerId) {
        UserProfile partner = userProfileRepository.findByUserId(partnerId);
        List<UserInterest> partnerInterests = userInterestRepository.findByUserProfile(partner);
        Collection<String> partnerInterestNames = new ArrayList<>();
        for (UserInterest partnerInterest : partnerInterests) {
            partnerInterestNames.add(partnerInterest.getInterest().getInterestName());
        }
        return partnerInterestNames;
    }
}
