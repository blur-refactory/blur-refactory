package com.blur.bluruser.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInterestCountDto {
	private String interestName;
    private Long count;
}
