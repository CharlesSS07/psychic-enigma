package com.psychicenigma.model;

import com.psychicenigma.OpenAIChatBot;
import com.psychicenigma.Settings;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class OpenAIClassifier {

    public List<String> options = new ArrayList<>();

    public OpenAIClassifier(Collection<String> options) {
        this.options.addAll(options);
    }

    protected String generateOptionsList() {
        String ret = "";
        for (int i = 0;i<options.size();i++) {
            ret += "Answer #"+ i + ") " + options.get(i) + '\n';
        }
        return ret;
    }
    public OpenAIChatMessageWrapper classify() {

        ConversationInterface<OpenAIChatMessageWrapper> conv = generateConversation();

        conv.addMessage(
                new OpenAIChatMessageWrapper(
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), "End of Conversation.")));
        conv.addMessage(
                new OpenAIChatMessageWrapper(
                        new ChatMessage(ChatMessageRole.SYSTEM.value(),
                                "You must classify the transcripts of the conversation according to the following options.\n\n"+
                                        generateOptionsList())));

        conv.addMessage(
                new OpenAIChatMessageWrapper(
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), "Respond in JSON format with the answer number.")));

        System.out.println(conv.transcriptString());

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(conv.getMessages().stream().map(OpenAIChatMessageWrapper::getChatMessage).toList())
                .n(1)
                .maxTokens(1000)
                .logitBias(new HashMap<>()).responseFormat("json_object")
                .build();

        ChatMessage responseMessage = Settings.OPEN_AI_SERVICE.createChatCompletion(chatCompletionRequest)
                .getChoices().getFirst().getMessage();
        responseMessage.setName("Classifier");

        return new OpenAIChatMessageWrapper(responseMessage);

    }

    public abstract ConversationInterface<OpenAIChatMessageWrapper> generateConversation();
}
