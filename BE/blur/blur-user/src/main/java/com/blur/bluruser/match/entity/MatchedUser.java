package com.blur.bluruser.match.entity;

import com.blur.bluruser.profile.entity.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "matched_user")
public class MatchedUser {

    @Id
    private String userId;

    private List<String> matchedList;

    public void update(List<String> matchedList) {
        this.matchedList = matchedList;
    }
}
