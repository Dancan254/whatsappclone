package com.mongs.whatsappclone.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(name = UserConstants.FIND_USER_BY_EMAIL)
    Optional<User> findByEmail(String email);

    @Query(name = UserConstants.FIND_USER_BY_PUBLIC_ID)
    Optional<User> findByUserId(String publicId);

    @Query(name = UserConstants.FIND_ALL_USERS_EXCEPT_SELF)
    List<User> findAllUsersExceptSelf(Authentication authentication);
}
