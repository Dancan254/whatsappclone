package com.mongs.whatsappclone.chat;

import com.mongs.whatsappclone.user.User;
import com.mongs.whatsappclone.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper mapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId(Authentication authenticatedUser){

        final String userId = authenticatedUser.getName();
        return chatRepository.findChatsBySenderId(userId)
                .stream()
                .map(chat -> mapper.toChatResponse(chat, userId))
                .toList();
    }

    public String createChat(String senderId, String recipientId){
        Optional<Chat> existingChat = chatRepository.findChatBySenderIdAndRecipientId(senderId, recipientId);
        if (existingChat.isPresent()){
            return existingChat.get().getId();
        }

        User sender = userRepository.findByUserId(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        User receiver = userRepository.findByUserId(recipientId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);

        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();
    }
}
