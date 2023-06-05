package com.blur.blurmatch.controller;

import com.blur.blurmatch.dto.MatchSettingDto;
import com.blur.blurmatch.dto.MeetingDto;
import com.blur.blurmatch.dto.ReportDto;
import com.blur.blurmatch.dto.request.RequestAcceptDto;
import com.blur.blurmatch.dto.request.RequestCheckDto;
import com.blur.blurmatch.dto.request.RequestMatchDto;
import com.blur.blurmatch.dto.response.ResponseAceeptDto;
import com.blur.blurmatch.dto.response.ResponseMatchDto;
import com.blur.blurmatch.service.MatchService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@Api(value = "매칭", description = "매칭 관련 API")
public class MatchingController {

    @Autowired
    private MatchService matchService;

    @ApiOperation(value = "매칭 설정 정보 가져오기", response = MatchSettingDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 설정 정보 조회 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/getSetting")
    public ResponseEntity<?> getSetting(@ApiParam(value = "유저 ID", required = true) @RequestParam("userId") String userId) {
        MatchSettingDto matchSettingDto = matchService.getSetting(userId);
        return ResponseEntity.status(HttpStatus.OK).body(matchSettingDto);
    }

    @ApiOperation(value = "매칭 설정 정보 생성", response = MatchSettingDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "매칭 설정 정보 생성 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/saveSetting")
    public ResponseEntity<?> createSetting(@RequestBody MatchSettingDto matchSettingDto) {
        matchService.createSetting(matchSettingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @ApiOperation(value = "매칭 설정 정보 수정", response = MatchSettingDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 설정 정보 수정 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/updateSetting")
    public ResponseEntity<?> updateSetting(@RequestBody MatchSettingDto matchSettingDto) {

        matchService.updateSetting(matchSettingDto);
        String res = "success";
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @ApiOperation(value = "매치 세팅 확인", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매치 세팅 확인"),
    })
    @GetMapping("/settingCheck")
    public ResponseEntity<Integer> settingCheck(
            @ApiParam(value = "사용자의 ID", required = true) @RequestParam("id") String userId) {

        Integer res = matchService.settingCheck(userId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @ApiOperation(value = "매칭 시작", response = ResponseMatchDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 시작 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/start")
    public ResponseEntity<?> matchStart(@RequestBody RequestMatchDto requestMatchDto) {

        ResponseMatchDto responseMatchDto = matchService.matchStart(requestMatchDto);
        if (responseMatchDto == null) {
            return ResponseEntity.status(403).body("신고10회이상");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseMatchDto);
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
    public ResponseEntity<?> matchStop(@ApiParam(value = "유저 ID", required = true) @RequestParam("userId") String userId,
                                       @ApiParam(value = "성별 (M/F)", required = true) @RequestParam("gender") String gender) {

        if (gender.equals("M")) {
            matchService.matchDecline(userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "매칭 상태 확인", response = ResponseMatchDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "매칭 상태 확인 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/check")
    public ResponseEntity<?> matchCheck(@RequestBody RequestCheckDto requestCheckDto) {

        String gender = requestCheckDto.getGender();
        if (gender.equals("F")) {
            RequestMatchDto requestMatchDto = new ModelMapper().map(requestCheckDto, RequestMatchDto.class);
            ResponseMatchDto responseMatchDto = matchService.matchStart(requestMatchDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseMatchDto);
        }
        String userId = requestCheckDto.getUserId();
        ResponseMatchDto responseMatchDto = matchService.matchCheck(userId, gender);
        return ResponseEntity.status(HttpStatus.OK).body(responseMatchDto);
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
    public ResponseEntity<?> matchAccept(@RequestBody RequestAcceptDto requestAcceptDto) {

        ResponseAceeptDto responseAceeptDto = matchService.matchAccept(requestAcceptDto);
        if (responseAceeptDto == null) {
            return ResponseEntity.status(404).body("Failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseAceeptDto);
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
    public ResponseEntity<?> matchDecline(@RequestBody RequestAcceptDto requestAcceptDto) {

        if(requestAcceptDto.getMyGender() == "M") {
            String userId = requestAcceptDto.getUserId();
            matchService.matchDecline(userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ApiOperation(value = "미팅 종료", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "미팅 종료 성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/exit")
    public ResponseEntity<?> meetingExit(@RequestBody MeetingDto meetingDto) {

        String res = matchService.meetingExit(meetingDto);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @ApiOperation(value = "신고하기", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "신고성공"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/report")
    public ResponseEntity<?> meetingReport(@RequestBody ReportDto reportDto) {

        matchService.meetingReport(reportDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
