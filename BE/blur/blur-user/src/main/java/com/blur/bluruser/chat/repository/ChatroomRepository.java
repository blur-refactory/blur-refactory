package com.blur.bluruser.chat.repository;

import com.blur.bluruser.chat.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, String> {
    Optional<Chatroom> findById(String roomId);
    Optional<List<Chatroom>> findByMaleId(String maleId);
    Optional<List<Chatroom>> findByFemaleId(String femaleId);
}
