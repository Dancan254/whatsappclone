package com.mongs.whatsappclone.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsersExceptSelf(Authentication authentication) {
        return userRepository.findAllUsersExceptSelf(authentication)
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
