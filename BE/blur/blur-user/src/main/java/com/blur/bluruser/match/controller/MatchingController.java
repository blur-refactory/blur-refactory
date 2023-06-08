package com.blur.bluruser.match.controller;

import com.blur.bluruser.match.dto.request.RequestAcceptDto;
import com.blur.bluruser.match.dto.request.RequestFemaleCheckDto;
import com.blur.bluruser.match.dto.request.RequestMatchDto;
import com.blur.bluruser.match.dto.request.RequestUpdateSettingDto;
import com.blur.bluruser.match.dto.response.ResponseAceeptDto;
import com.blur.bluruser.match.dto.response.ResponseCheckDto;
import com.blur.bluruser.match.dto.response.ResponseMatchSettingDto;
import com.blur.bluruser.match.dto.response.ResponseStartDto;
import com.blur.bluruser.match.service.MatchService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
@Api(value = "매칭", description = "매칭 관련 API")
public class MatchingController {

    private final MatchService matchService;

    @ApiOperation(value = "매칭 설정 정보 가져오기", response = ResponseMatchSettingDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 설정 정보 조회 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/getSetting")
    public ResponseEntity<?> getSetting(@ApiParam(value = "사용자의 ID", required = true)
                                            @RequestHeader("X-Username") String userId) {
        ResponseMatchSettingDto responseMatchSettingDto = matchService.getSetting(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseMatchSettingDto);
    }

    @ApiOperation(value = "매칭 설정 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "매칭 설정 정보 수정 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/updateSetting")
    public ResponseEntity<?> updateSetting(@ApiParam(value = "사용자의 ID", required = true)
                                               @RequestHeader("X-Username") String userId,
                                           @ApiParam(value = "변경 매치 설정 정보", required = true)
                                                @RequestBody RequestUpdateSettingDto requestUpdateSettingDto) {
        matchService.updateSetting(userId, requestUpdateSettingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ApiOperation(value = "매칭 시작", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 시작 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/start")
    public ResponseEntity<?> matchStart(@ApiParam(value = "사용자의 ID", required = true)
                                            @RequestHeader("X-Username") String userId,
                                        @ApiParam(value = "요청 매치 정보", required = true)
                                            @RequestBody RequestMatchDto requestMatchDto) {

        ResponseStartDto responseStartDto = matchService.matchStart(userId, requestMatchDto);

        if (responseStartDto == null) {
            return ResponseEntity.status(403).body("신고10회이상");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseStartDto);
    }

    @ApiOperation(value = "여성 유저 매칭 확인", response = ResponseCheckDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여성 유저 매칭 확인 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/femaleCheck")
    public ResponseEntity<?> femaleCheck(@ApiParam(value = "사용자의 ID", required = true)
                                        @RequestHeader("X-Username") String userId,
                                        @ApiParam(value = "여성 정보", required = true)
                                        @RequestBody RequestFemaleCheckDto requestFemaleCheckDto) {

        ResponseCheckDto responseCheckDto = matchService.femaleCheck(userId, requestFemaleCheckDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseCheckDto);
    }

    @ApiOperation(value = "매칭 시작", response = ResponseCheckDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 시작 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/maleCheck")
    public ResponseEntity<?> maleCheck(@ApiParam(value = "사용자의 ID", required = true)
                                         @RequestHeader("X-Username") String userId) {

        ResponseCheckDto responseCheckDto = matchService.maleCheck(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseCheckDto);
    }

    @ApiOperation(value = "매칭 종료", response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 종료 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/stop")
    public ResponseEntity<?> matchStop(@ApiParam(value = "사용자의 ID", required = true)
                                           @RequestHeader("X-Username") String userId) {

        matchService.matchDecline(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "매칭 거절", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 거절 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/decline")
    public ResponseEntity<?> matchDecline(@ApiParam(value = "사용자의 ID", required = true)
                                              @RequestHeader("X-Username") String userId) {

        matchService.matchDecline(userId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "매칭 수락", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 수락 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/accept")
    public ResponseEntity<?> matchAccept(@ApiParam(value = "사용자의 ID", required = true)
                                             @RequestHeader("X-Username") String userId,
                                         @RequestBody RequestAcceptDto requestAcceptDto) {

        ResponseAceeptDto responseAceeptDto = matchService.matchAccept(userId, requestAcceptDto);
        if (responseAceeptDto == null) {
            return ResponseEntity.status(404).body("Failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseAceeptDto);
    }


}
