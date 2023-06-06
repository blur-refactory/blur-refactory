package com.blur.bluruser.chat.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "chatroom")
@AllArgsConstructor
public class Chatroom {
    @Id
    @Column(name = "chatroom_id")
    private String id;
    private String maleName;

    private String femaleName;

    private String maleEmail;

    private String femaleEmail;

    @OneToMany(mappedBy = "chatroom")
    private List<Chat> chats = new ArrayList<>();
}
