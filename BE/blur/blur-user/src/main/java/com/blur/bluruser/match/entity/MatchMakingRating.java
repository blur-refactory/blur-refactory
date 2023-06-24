package com.blur.bluruser.match.entity;

import com.blur.bluruser.profile.entity.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Table(name = "matchmaking_rating")
public class MatchMakingRating {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "point", columnDefinition = "integer default 1000")
    private Integer point;

    @Column(name = "winning_streak", columnDefinition = "integer default 0")
    private Integer winningStreak;

    @Column(name = "losing_streak", columnDefinition = "integer default 0")
    private Integer losingStreak;

    @Column(name = "report_count", columnDefinition = "integer default 0")
    private Integer reportCount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    public void update(Integer point, Integer winningStreak, Integer losingStreak) {
        this.point = point;
        this.winningStreak = winningStreak;
        this.losingStreak = losingStreak;
    }

    public void report(Integer reportCount) {
        this.reportCount = reportCount;
    }
}