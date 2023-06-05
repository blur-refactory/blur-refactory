package com.blur.blurmatch.entity;

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
@Builder
@Table(name = "matched_user")
public class MatchedUser {

    @JsonIgnore
    @Column(name = "user_Id")
    @Id
    private String userId;

    @ElementCollection
    @Column(name = "matched_list")
    private Collection<String> matchedList = new ArrayList<>();

    @Builder
    public MatchedUser(String userId, Collection<String> matchedList) {
        this.userId = userId;
        this.matchedList = matchedList;
    }

    public void update(Collection<String> matchedList) {
        this.matchedList = matchedList;
    }
}
