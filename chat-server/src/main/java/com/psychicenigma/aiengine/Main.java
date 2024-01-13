package com.psychicenigma.aiengine;


import com.psychicenigma.aiengine.model.*;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;


public class Main {
    public static void main(String[] args) {
        ConversationInterface<OpenAIChatMessageWrapper> conversation = new Conversation<OpenAIChatMessageWrapper>();
        OpenAIChatMessageWrapper document = new OpenAIChatMessageWrapper(
                new ChatMessage(ChatMessageRole.SYSTEM.value(), "", "Document"));
        OpenAIChatBot documentEditorBot = null;

        documentEditorBot = new OpenAIChatBot("JJ") {

            @Override
            public ConversationInterface<OpenAIChatMessageWrapper> generateConversation() {
                Conversation<OpenAIChatMessageWrapper> tmpConversation = new Conversation<>();
                ChatMessage systemMessage = new ChatMessage(
                        ChatMessageRole.SYSTEM.value(),
                        "You are an assistant who helps a user write a document. Surround any modifications to the document you make in \"MODIFIED(...)\".",
                        "System"
                );
                tmpConversation.addMessage(new OpenAIChatMessageWrapper(systemMessage));
                tmpConversation.addMessage(document);
                tmpConversation.extend(conversation);
                return tmpConversation;
            }

        };

        MessageInterface incoming = new MessageInterface() {

            @Override
            public String getContent() {
                return "How do I blend these paragraphs together?";
            }

            @Override
            public String getSender() {
                return "Jim";
            }

            @Override
            public String getRole() {
                return ChatMessageRole.USER.value();
            }

        };

        conversation.addMessage(
                new OpenAIChatMessageWrapper(Message.toChatMessage(incoming, ChatMessageRole.USER.value())));
        OpenAIChatMessageWrapper response = documentEditorBot.respond();
        conversation.addMessage(response);

        System.out.println(conversation.transcriptString());
    }
}