package com.mongs.whatsappclone.message;

import com.mongs.whatsappclone.chat.Chat;
import com.mongs.whatsappclone.chat.ChatRepository;
import com.mongs.whatsappclone.file.FileService;
import com.mongs.whatsappclone.notification.Notification;
import com.mongs.whatsappclone.notification.NotificationService;
import com.mongs.whatsappclone.notification.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper mapper;
    private final FileService fileService;
    private final NotificationService notificationService;


    public void save(MessageRequest messageRequest){
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        Message message = new Message();
        message.setChat(chat);
        message.setContent(messageRequest.getContent());
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getRecipientId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .chatName(chat.getChatName(message.getSenderId()))
                .messageType(messageRequest.getType())
                .notificationType(NotificationType.MESSAGE)
                .build();
        notificationService.sendNotification(chat.getRecipient().getId(), notification);
    }

    public List<MessageResponse> findChatMessages(String chatId){
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(mapper::toMessageResponse)
                .toList();
    }

    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        //final String recipientId = getRecipientId(chat, authentication);
        messageRepository.setMessagesToBeSeenByChatId(chatId, MessageState.SEEN);

        //todo notification
    }
    private String getRecipientId(Chat chat, Authentication authentication){
        if(chat.getSender().getId().equals(authentication.getName())){
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }

    public void uploadMediaMessage(String chatId, Authentication authentication, MultipartFile file){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        final String recipientId = getRecipientId(chat, authentication);
        final String senderId = getSenderId(chat, authentication);
        final  String filePath = fileService.saveFile(file,senderId);

        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setReceiverId(recipientId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaPath(filePath);
        messageRepository.save(message);

        //todo notification

    }

    private String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())){
            return chat.getSender().getId();
        } else {
            return chat.getRecipient().getId();
        }
    }
}
