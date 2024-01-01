package com.psychicenigma.chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@SpringBootApplication
//@EnableWebSocket
//@EnableWebSocketMessageBroker
public class ChatApplication {

    public static void main(String[] args) {
        System.out.println("starting");
        SpringApplication.run(ChatApplication.class, args);
    }

}

@Controller
class ChatController {

    @MessageMapping("/chat-message")
    @SendTo("/topic/chat-message")
    public String handleChatMessage(String message) {
        System.out.println("message: " + message);
        return message;
    }
}


