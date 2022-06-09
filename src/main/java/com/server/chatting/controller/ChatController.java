package com.server.chatting.controller;

import com.server.chatting.dto.ChatMessage;
import com.server.chatting.dto.ChatRoom;
import com.server.chatting.repository.ChatRoomRepository;
import com.server.chatting.service.RedisPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
   private final RedisPublisher redisPublisher;
   private final ChatRoomRepository chatRoomRepository;

    /**
     * websocker /pub/chat/message로 들어오는 메시징을 처리한다.
     */
   @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
       if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
           chatRoomRepository.enterChatRoom(message.getRoomId());
           message.setMessage(message.getSender() + "님이 입장하셨습니다.");
       }

       // Web socket에 발행된 메시지를 redis로 발행한다(publish)
       redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
   }
}
