package com.blur.blurmatch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@NoArgsConstructor
@Builder
@Table(name = "matched_user")
public class Report {

    @JsonIgnore
    @Column(name = "user_Id")
    @Id
    private String userId;

    @Column(name = "report_userId")
    private String reportedUserId;

    @Column(name = "content")
    private String content;

    @Builder
    public Report(String userId, String reportUserId, String content) {
        this.userId = userId;
        this.reportedUserId = reportUserId;
        this.content = content;
    }
}
