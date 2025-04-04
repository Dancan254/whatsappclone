package com.mongs.whatsappclone.message;

import com.mongs.whatsappclone.base.BaseAuditingEntity;
import com.mongs.whatsappclone.chat.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "mesasges")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@NamedQuery(
        name = MessageConstants.FIND_MESSAGES_BY_CHAT_ID,
        query = "select m from Message m where m.chat.id = :chatId ORDER BY m.createdDate"
)
@NamedQuery(
        name = MessageConstants.SET_MESSAGES_TO_BE_SEEN_BY_CHAT,
        query = "UPDATE Message SET state = :newState WHERE chat.id = :chatId"
)
public class Message extends BaseAuditingEntity {

    @Id
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageState state;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    @Column(nullable = false, name = "receiver_id")
    private String receiverId;
    @Column(nullable = false, name = "sender_id")
    private String senderId;
    private String mediaPath;
}
