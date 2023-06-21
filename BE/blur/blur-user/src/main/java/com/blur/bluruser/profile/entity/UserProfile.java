package com.blur.bluruser.profile.entity;

import com.blur.bluruser.match.entity.MatchMakingRating;
import com.blur.bluruser.match.entity.MatchSetting;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "image")
    private String image;

    @Column(name = "gender")
    private String gender;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "mbti")
    private String mbti;

    @JsonManagedReference
    @OneToMany(mappedBy = "userProfile")
    private List<UserInterest> userInterests;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private MatchMakingRating matchMakingRating;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private MatchSetting matchSetting;

    public void updateProfile(Integer age, String nickname, String gender, String introduce, String mbti) {
        this.age = age;
        this.nickname = nickname;
        this.gender = gender;
        this.introduce = introduce;
        this.mbti = mbti;
    }

    public void updateImage(String image) {
        this.image = image;
    }

}
