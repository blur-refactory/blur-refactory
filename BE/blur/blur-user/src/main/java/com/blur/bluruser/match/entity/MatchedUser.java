package com.blur.bluruser.match.entity;

import com.blur.bluruser.profile.entity.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "matched_user")
public class MatchedUser {

    @Id
    @Column(name = "user_id")
    private String userId;

    @ElementCollection
    @Column(name = "matched_list")
    private Collection<String> matchedList = new ArrayList<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    public void update(Collection<String> matchedList) {
        this.matchedList = matchedList;
    }
}
