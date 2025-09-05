package com.example.back_end.repo;

import com.example.back_end.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    // Get all messages for an Ad between buyer & seller
    List<Message> findByAd_AdIdAndSender_UserIdAndReceiver_UserId(
            Long adId, Long senderId, Long receiverId);

    // Get chat history for one Ad
    List<Message> findByAd_AdIdOrderBySentDateAsc(Long adId);
}
