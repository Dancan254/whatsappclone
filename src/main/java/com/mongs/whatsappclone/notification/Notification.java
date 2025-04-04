package com.mongs.whatsappclone.notification;

import com.mongs.whatsappclone.message.MessageType;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Notification {
    private String chatId;
    private String content;
    private String senderId;
    private String chatName;
    private MessageType messageType;
    private NotificationType notificationType;
    private byte[] media;

}
