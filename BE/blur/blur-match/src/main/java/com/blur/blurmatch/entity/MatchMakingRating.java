package com.blur.blurmatch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@Table(name = "matchmaking_rating")
public class MatchMakingRating {

    @JsonIgnore
    @Column(name = "user_Id")
    @Id
    private String userId;

    @Column(name = "point")
    private Integer point;

    @Column(name = "winning_streak")
    private Integer winningStreak;

    @Column(name = "losing_streak")
    private Integer losingStreak;

    @Column(name = "report_count")
    private Integer reportCount;


    @Builder
    public MatchMakingRating(String  userId, Integer point, Integer winningStreak, Integer losingStreak, Integer reportCount) {
        this.userId = userId;
        this.point = point;
        this.winningStreak = winningStreak;
        this.losingStreak = losingStreak;
        this.reportCount = reportCount;
    }

    public void update(Integer point, Integer winningStreak, Integer losingStreak) {
        this.point = point;
        this.winningStreak = winningStreak;
        this.losingStreak = losingStreak;
    }

    public void report(Integer reportCount) {
        this.reportCount = reportCount;
    }
}