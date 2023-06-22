package com.blur.bluruser.profile.controller;

import com.blur.bluruser.profile.dto.request.RequestProfileSettingDto;
import com.blur.bluruser.profile.dto.request.RequestUserInterestDto;
import com.blur.bluruser.profile.dto.response.ResponseCardDto;
import com.blur.bluruser.profile.dto.response.ResponseInterestDto;
import com.blur.bluruser.profile.dto.response.ResponseProfileSettingDto;
import com.blur.bluruser.profile.repository.InterestRepository;
import com.blur.bluruser.profile.service.ProfileService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Api(value = "프로필", description = "프로필 관련 API")
public class ProfileController {

    private final ProfileService profileService;

    private final InterestRepository interestRepository;

    @ApiOperation(value = "프로필 유무 확인", response = ResponseCardDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 유무 확인"),
    })
    @GetMapping("/check")
    public ResponseEntity<Boolean> check(
            @ApiParam(value = "사용자의 ID", required = true) @RequestHeader("X-Username") String userId) {

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
            @ApiParam(value = "사용자의 ID", required = true) @RequestHeader("X-Username") String userId) {

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
                                                                           @RequestHeader("X-Username") String userId) {

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
    public ResponseEntity<?> updateProfile(@ApiParam(value = "사용자의 ID", required = true)
                                               @RequestHeader("X-Username") String userId,
                                            @ApiParam(value = "변경 프로필 정보", required = true)
                                               @RequestBody RequestProfileSettingDto requestProfileSettingDto) {
        RequestProfileSettingDto profile = profileService.updateProfile(userId, requestProfileSettingDto);
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
    public ResponseEntity<String> updateImage(@ApiParam(value = "사용자의 ID", required = true)
                                                  @RequestHeader("X-Username") String userId,
                                              @ApiParam(value = "프로필 이미지", required = true)
                                              @RequestParam("profileImage") MultipartFile profileImage) throws IOException{
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
    public ResponseEntity<ResponseInterestDto> getInterests(@ApiParam(value = "사용자의 ID", required = true)
                                                                @RequestHeader("X-Username") String userId) {
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
    public ResponseEntity<?> updateInterest(@ApiParam(value = "관심사 변경 정보", required = true)
                                                @RequestBody RequestUserInterestDto requestUserInterestDto,
                                            @ApiParam(value = "사용자의 ID", required = true)
                                            @RequestHeader("X-Username") String userId) throws Exception {

        profileService.updateInterest(requestUserInterestDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
