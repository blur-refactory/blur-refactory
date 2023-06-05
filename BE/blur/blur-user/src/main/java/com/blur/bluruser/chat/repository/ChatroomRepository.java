package com.blur.bluruser.chat.repository;

import com.blur.chat.api.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    Optional<Chatroom> findBy
}
