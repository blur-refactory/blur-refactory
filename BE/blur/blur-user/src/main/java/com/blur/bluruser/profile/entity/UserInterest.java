package com.blur.bluruser.profile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_interest")
public class UserInterest {

    @Id
    private String userId;

    private List<String> interests;

    public void update(List<String> interests) {
        this.interests = interests;
    }
}
