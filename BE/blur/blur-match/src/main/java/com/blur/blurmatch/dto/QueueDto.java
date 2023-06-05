package com.blur.blurmatch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueDto {

    private String userId;

    private Integer point;

    public QueueDto(MatchDto matchDto) {
        this.userId = matchDto.getUserId();
        this.point = matchDto.getPoint();
    }
}
