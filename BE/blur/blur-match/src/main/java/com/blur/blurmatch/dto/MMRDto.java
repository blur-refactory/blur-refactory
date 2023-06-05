package com.blur.blurmatch.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@Setter
public class MMRDto {

    private String userId;

    private Integer point;

    private Integer winningStreak;

    private Integer losingStreak;

    private Integer reportCount;

}
