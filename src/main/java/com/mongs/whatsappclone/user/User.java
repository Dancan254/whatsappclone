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
@NamedQuery(
        name=UserConstants.FIND_USER_BY_EMAIL,
        query = "SELECT u FROM User u WHERE u.email = :email"
)
@NamedQuery(
        name=UserConstants.FIND_ALL_USERS_EXCEPT_SELF,
        query = "SELECT u FROM User u WHERE u.id != :publicId"
)
@NamedQuery(
        name = UserConstants.FIND_USER_BY_PUBLIC_ID,
        query = "select u from User u where u.id = :publicId"
)
public class User extends BaseAuditingEntity {
    private static final long LAST_ACTIVE_INTERVAL = 5;

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

    @Transient
    public boolean isUserOnline() {
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

}
