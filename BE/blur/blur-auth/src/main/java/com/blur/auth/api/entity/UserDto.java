package com.blur.auth.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class UserDto {

    private String userId;

    private String password;

    private String email;

//    private String gender;

    /* DTO -> Entity */
    public User toEntity() {
    	User user = User.builder()
                .userId(userId)
                .password(password)
                .email(email)
                .build();
        return user;
    }

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

	@Override
	public String toString() {
		return "UserDto [userId=" + userId + ", password=" + password + ", email=" + email + "]";
	}
}
