package com.mongs.whatsappclone.user;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDateTime lastSeen;
    private Boolean isOnline;
}
