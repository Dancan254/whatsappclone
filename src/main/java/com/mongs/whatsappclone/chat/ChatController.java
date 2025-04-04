package com.mongs.whatsappclone.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<StringResponse> createChat(
            @RequestParam(name = "senderId") String senderId,
            @RequestParam(name = "recipientId") String recipientId)
    {
        final String chatId = chatService.createChat(senderId, recipientId);
        StringResponse stringResponse = StringResponse.builder()
                .response(chatId)
                .build();
        return ResponseEntity.ok(stringResponse);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(Authentication authentication){
        return ResponseEntity.ok(chatService.getChatsByReceiverId(authentication));
    }
}
