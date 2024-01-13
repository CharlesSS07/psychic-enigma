package com.psychicenigma.chatserver;

import com.psychicenigma.aiengine.OpenAIChatBot;
import com.psychicenigma.chatserver.model.MessageCodec;
import com.psychicenigma.aiengine.model.*;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

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

    private OpenAIChatBot documentEditorBot;

    private OpenAIClassifier enoughInfoClassifier;
    private final OpenAIClassifier.OutcomeOption enoughInfo = new OpenAIClassifier.OutcomeOption(
            "The user has supplied enough information to modify the document accordingly.");
    private final OpenAIClassifier.OutcomeOption notEnoughInfo = new OpenAIClassifier.OutcomeOption(
            "The user has not supplied enough information to modify the document accordingly.");

    private OpenAIChatBot nextStepReccommenderBot;

    ConversationInterface<OpenAIChatMessageWrapper> conversation = new Conversation<OpenAIChatMessageWrapper>();
    OpenAIChatMessageWrapper document = new OpenAIChatMessageWrapper(new ChatMessage(ChatMessageRole.SYSTEM.value(), "", "Document"));

    ChatController() {

        documentEditorBot = new OpenAIChatBot("JJ") {

            @Override
            public ConversationInterface<OpenAIChatMessageWrapper> generateConversation() {
                Conversation<OpenAIChatMessageWrapper> tmpConversation = new Conversation<>();
                ChatMessage systemMessage = new ChatMessage(
                        ChatMessageRole.SYSTEM.value(),
                        "You are an assistant named JJ who helps a user write a document. Always respond in HTML.",
                        "System"
                );
                tmpConversation.addMessage(new OpenAIChatMessageWrapper(systemMessage));
                tmpConversation.addMessage(document);
                tmpConversation.extend(conversation);
                return tmpConversation;
            }

        };


        List<OpenAIClassifier.OutcomeOption> options = new ArrayList<>();
        options.add(enoughInfo);
        options.add(notEnoughInfo);
        options.add(OpenAIClassifier.NONE_OF_THE_ABOVE);
        enoughInfoClassifier = new OpenAIClassifier(options) {

            @Override
            public ConversationInterface<OpenAIChatMessageWrapper> generateConversation() {
                Conversation<OpenAIChatMessageWrapper> tmpConversation = new Conversation<>();
                ChatMessage systemMessage = new ChatMessage(
                        ChatMessageRole.SYSTEM.value(),
                        "Document:",
                        "System"
                );
                tmpConversation.addMessage(new OpenAIChatMessageWrapper(systemMessage));
                tmpConversation.addMessage(document);
                tmpConversation.extend(conversation);
                return tmpConversation;
            }

        };

        nextStepReccommenderBot = new OpenAIChatBot("Brad") {

            @Override
            public ConversationInterface<OpenAIChatMessageWrapper> generateConversation() {
                Conversation<OpenAIChatMessageWrapper> tmpConversation = new Conversation<>();
                ChatMessage systemMessage = new ChatMessage(
                        ChatMessageRole.SYSTEM.value(),
                        "You are an assistant named Brad who is responsible for informing the user of their choices. Given the conversation, briefly tell the user how they could improve their document. Possible options could include, but are not limited to, \"Write another paragraph\", \"Expand on paragraph X\", \"Refute this point\", \"Eat my ass\", \"Let's chat about where this should go next\" or \"Generate citations\". Be creative, helpful, and brief when specifying the options the user could choose. Always respond with HTML. Respond in a numbered list format.",
                        "System"
                );
                tmpConversation.addMessage(new OpenAIChatMessageWrapper(systemMessage));
                tmpConversation.addMessage(document);
                tmpConversation.extend(conversation);
                return tmpConversation;
            }

        };
    }

    @MessageMapping("/chat-message")
//    @SendTo("/topic/chat-message")
    public void handleChatMessage(String json) {
        // forward back to user
        simpMessagingTemplate.convertAndSend("/topic/chat-message", json);

        try {

            MessageInterface message = MessageCodec.fromJSON(json);

            // Determine if user has supplied enough information for AI to get to work.
            conversation.addMessage(
                    new OpenAIChatMessageWrapper(Message.toChatMessage(message, ChatMessageRole.USER.value())));
            OpenAIClassifier.OutcomeOption result = enoughInfoClassifier.classify(5);


            if (result == enoughInfo) {
                OpenAIChatMessageWrapper editorResponse = documentEditorBot.respond();
                conversation.addMessage(editorResponse);

                simpMessagingTemplate.convertAndSend(
                        "/topic/chat-message", new MessageCodec(editorResponse).toJSON());
            } else if (result == notEnoughInfo) {
                // TODO: generate an explainer and send it back (explain what is missing)
                OpenAIChatMessageWrapper notEnoughInfoMessage = new OpenAIChatMessageWrapper(
                        new ChatMessage(
                                ChatMessageRole.ASSISTANT.value(),
                                "Sorry, but I could not complete this request as there is not enough information. Please give me more to work with."
                        )
                );
                conversation.addMessage(notEnoughInfoMessage);
                simpMessagingTemplate.convertAndSend(
                        "/topic/chat-message", new MessageCodec(notEnoughInfoMessage).toJSON());
            } else if (result == OpenAIClassifier.NONE_OF_THE_ABOVE) {
//                throw new IllegalStateException("This should not happen");
            } else if (result == OpenAIClassifier.NO_OPTION_SELECTED) {
//                throw new IllegalStateException("This should not happen");
            } else {
                throw new IllegalStateException("This should not happen");
            }

            OpenAIChatMessageWrapper nextStepResponse = nextStepReccommenderBot.respond();

            simpMessagingTemplate.convertAndSend(
                    "/topic/chat-message", new MessageCodec(nextStepResponse).toJSON());

            System.out.println(documentEditorBot.generateConversation().transcriptString());

            System.out.println(nextStepResponse.getContent());
        } catch (Exception e) {

            simpMessagingTemplate.convertAndSend("/topic/chat-message", new MessageCodec(
                    new OpenAIChatMessageWrapper(
                            new ChatMessage(
                                    ChatMessageRole.ASSISTANT.value(),
                                    "There was a problem while processing the chat:\n" +
                                            e.getClass() + ": " + e.getMessage()
                            )
                    )
            ).toJSON());
            e.printStackTrace();
        }
    }

    @MessageMapping("/document-sync")
    public void handleDocumentSync(String innerHtml) {

        document.getChatMessage().setContent(innerHtml);

        System.out.println("Synced Document");

        System.out.println(documentEditorBot.generateConversation().transcriptString());

    }
}


