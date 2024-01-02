package com.psychicenigma;


import com.psychicenigma.model.Message;
import com.psychicenigma.model.MessageInterface;

public class Main {
    public static void main(String[] args) {
        OpenAIChatBot bot = new OpenAIChatBot();
        MessageInterface message = new MessageInterface() {

            @Override
            public String getContent() {
                return "How do I blend these paragraphs together?";
            }

            @Override
            public String getSender() {
                return "Jim";
            }

        };

        System.out.println(message);
        System.out.println(bot.send(message));
    }
}