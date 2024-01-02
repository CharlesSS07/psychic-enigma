package com.psychicenigma.chatserver;

import com.psychicenigma.OpenAIChatBot;
import com.psychicenigma.chatserver.model.MessageCodec;
import com.psychicenigma.model.MessageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private OpenAIChatBot bot;

    ChatController() {
        bot = new OpenAIChatBot();
    }

    @MessageMapping("/chat-message")
    @SendTo("/topic/chat-message")
    public String handleChatMessage(String json) {
        System.out.println("message: " + json);

        MessageInterface message = MessageCodec.fromJSON(json);
        simpMessagingTemplate.convertAndSend("/topic/chat-message", json);

        MessageInterface response = bot.send(message);

        System.out.println(bot.getConversation().transcriptString());

        return new MessageCodec(response).toJSON();
    }
}


