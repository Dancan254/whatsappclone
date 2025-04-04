package com.mongs.whatsappclone.message;

import com.mongs.whatsappclone.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(name = MessageConstants.FIND_MESSAGES_BY_CHAT_ID)
    List<Message> findMessagesByChatId(String chatId);

    @Query(name = MessageConstants.SET_MESSAGES_TO_BE_SEEN_BY_CHAT)
    void setMessagesToBeSeenByChatId(@Param("chatId") String chatId, @Param("newState") MessageState messageState);
}
