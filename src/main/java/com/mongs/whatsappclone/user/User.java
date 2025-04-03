package com.mongs.whatsappclone.user;

import com.mongs.whatsappclone.base.BaseAuditingEntity;
import com.mongs.whatsappclone.chat.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class User extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chatAsSender;

    @OneToMany(mappedBy = "recipient")
    private List<Chat> chatAsReceiver;

}
