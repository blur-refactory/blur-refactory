package com.blur.blurprofile.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blur.blurprofile.dto.ProfileDto;
import com.blur.blurprofile.dto.request.RequestProfileSettingDto;
import com.blur.blurprofile.dto.request.RequestUserInterestDto;
import com.blur.blurprofile.dto.response.ResponseCardDto;
import com.blur.blurprofile.dto.response.ResponseInterestDto;
import com.blur.blurprofile.dto.response.ResponseProfileSettingDto;
import com.blur.blurprofile.entity.Interest;
import com.blur.blurprofile.repository.InterestRepository;
import com.blur.blurprofile.service.InterestService;
import com.blur.blurprofile.service.ProfileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/profile/{id}")
@Api(value = "프로필", description = "프로필 관련 API")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    InterestRepository interestRepository;
    
    @Autowired
    InterestService interestService;

    @PostMapping("/test")
    public void test(@PathVariable("id") String userId) {
        List<Interest> interestList = interestRepository.findAll();
        Integer num = interestList.size();
        Set<Interest> interests = new HashSet<Interest>();
        Random rand = new Random();
        while (interests.size() < 5) {
            Interest randomInterest = interestList.get(rand.nextInt(num));
            interests.add(randomInterest);
        }
    }

    @ApiOperation(value = "프로필 유무 확인", response = ResponseCardDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 유무 확인"),
    })
    @GetMapping("/check")
    public ResponseEntity<Boolean> check(
            @ApiParam(value = "사용자의 ID", required = true) @PathVariable("id") String userId) {

        Boolean res = profileService.check(userId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @ApiOperation(value = "카드 정보 가져오기", response = ResponseCardDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "카드 정보 가져오기 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<ResponseCardDto> getCard(
            @ApiParam(value = "사용자의 ID", required = true) @PathVariable("id") String userId) {

        ResponseCardDto responseCardDto = profileService.getCard(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseCardDto);
    }

    @ApiOperation(value = "프로필 세팅 가져오기", response = ResponseProfileSettingDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 세팅 가져오기 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/getProfile")
    public ResponseEntity<ResponseProfileSettingDto> getProfileSetting(@ApiParam(value = "User ID", required = true)
                                                                           @PathVariable("id") String userId) {

        ResponseProfileSettingDto responseProfileSettingDto = profileService.getProfileSetting(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseProfileSettingDto);
    }

    @ApiOperation(value = "프로필 설정 업데이트", response = RequestProfileSettingDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 설정 업데이트 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody RequestProfileSettingDto requestProfileSettingDto) throws Exception {
        RequestProfileSettingDto profile = profileService.updateProfile(requestProfileSettingDto);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @ApiOperation(value = "프로필 사진 업데이트", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 사진 업데이트 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/updateImage")
    public ResponseEntity<String> updateImage(@PathVariable("id") String userId, @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        String res = profileService.updateImage(userId, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @ApiOperation(value = "관심사 가져오기", response = ResponseInterestDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관심사 가져오기 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/getInterest")
    public ResponseEntity<ResponseInterestDto> getInterests(@ApiParam(value = "User ID", required = true) @PathVariable("id") String userId) {
        ResponseInterestDto responseInterestDto = profileService.getInterests(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseInterestDto);
    }

    @ApiOperation(value = "관심사 설정 업데이트", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관심사 설정 업데이트 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/updateInterest")
    public ResponseEntity<?> updateInterest(@RequestBody RequestUserInterestDto requestUserInterestDto,
                                            @ApiParam(value = "User ID", required = true) @PathVariable("id") String userId) throws Exception {

        profileService.updateInterest(requestUserInterestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "프로필", response = ProfileDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 가져오기 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/service")
    public ResponseEntity<ProfileDto> getProfile(@ApiParam(value = "User ID", required = true) @PathVariable("id") String userId) {

        ProfileDto profileDto = profileService.getProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(profileDto);
    }

    @GetMapping("/service/partner")
    public ResponseEntity<?> getPartnerInterest(@ApiParam(value = "Partner ID", required = true) @PathVariable("id") String partnerId) {

        Collection<String> partnerInterest = profileService.getPartnerInterest(partnerId);
        return ResponseEntity.status(HttpStatus.OK).body(partnerInterest);
    }
    
    
    
    @ApiOperation(value = "관심사 순위 가져오기", response = ResponseInterestDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관심사 순위 가져오기 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/getInterestRank")
    public ResponseEntity<?> getInterestRank(@RequestBody String interestName) {
    	System.out.println(interestService.findPopularInterestsByInterestName(interestName).toString());
        return ResponseEntity.status(HttpStatus.OK).body(interestService.findPopularInterestsByInterestName(interestName));
    }
}
