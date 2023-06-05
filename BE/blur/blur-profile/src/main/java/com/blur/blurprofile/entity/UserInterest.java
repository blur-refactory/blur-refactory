package com.blur.blurprofile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_interest")
public class UserInterest {

    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "interest_name")
    private Interest interest;


}
