package com.psychicenigma.aiengine.model;

import com.psychicenigma.aiengine.Settings;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.*;

import java.util.*;

public abstract class OpenAIClassifier {

    public static final OutcomeOption NONE_OF_THE_ABOVE = new OutcomeOption("None of the above.");
    public static final OutcomeOption NO_OPTION_SELECTED = new OutcomeOption("No option selected.");
    public static final class OutcomeOption {

        private String optionDescription;

        public OutcomeOption(String optionDescription) {
            this.optionDescription = optionDescription;
        }

        public String getOptionDescription() {
            return optionDescription;
        }

        protected String formatOption(int index) {
            return String.format("Answer #%02d) %s\n", index, getOptionDescription());
        }

    }

    private List<OutcomeOption> options = new ArrayList<>();

    public OpenAIClassifier(Collection<OutcomeOption> options) {
        this.options.addAll(options);
    }

    protected String generateOptionsList() {
        String ret = "";
        for (int i = 0;i<options.size();i++) {
            ret += options.get(i).formatOption(i+1);
        }
        return ret;
    }

    public void shuffleOptionOrder() {
        Collections.shuffle(options);
    }

    public OutcomeOption classify(int n) {

        if (n<=0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        ConversationInterface<OpenAIChatMessageWrapper> conv = generateConversation();

        conv.addMessage(
                new OpenAIChatMessageWrapper(
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), "End of Conversation.")));
        conv.addMessage(
                new OpenAIChatMessageWrapper(
                        new ChatMessage(ChatMessageRole.SYSTEM.value(),
                                "You must classify the transcripts of the conversation according to the following options.\n\n"+
                                        generateOptionsList())));

//        conv.addMessage(
//                new OpenAIChatMessageWrapper(
//                        new ChatMessage(ChatMessageRole.SYSTEM.value(), "Respond in JSON format with the answer number.")));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(conv.getMessages().stream().map(OpenAIChatMessageWrapper::getChatMessage).toList())
                .n(n)
                .maxTokens(10)
                .logitBias(new HashMap<>())//.responseFormat("json_object")
                .build();

        ChatCompletionResult completionRequest = Settings.OPEN_AI_SERVICE.createChatCompletion(chatCompletionRequest);

        List<OutcomeOption> selected = completionRequest.getChoices().stream().map( e-> matchOption(e.getMessage().getContent()) ).toList();

        if (selected.size()==1)
            return selected.getFirst();

        // ascertain the option to return

        HashMap<OutcomeOption, Integer> counts = new HashMap<>();
        for (OutcomeOption outcomeOption : selected) {
            counts.put(outcomeOption, counts.getOrDefault(outcomeOption, 0)+1);
        }

        if (counts.containsKey(OpenAIClassifier.NO_OPTION_SELECTED)) {
            counts.remove(OpenAIClassifier.NO_OPTION_SELECTED);
            if (counts.isEmpty())
                throw new IllegalStateException("Classifier failed to classify."); // should just return NO_OPTION_SELECTED...
        }

        OutcomeOption maxOption = null;
        int maxOptionCount = 0;
        for (OutcomeOption outcomeOption : counts.keySet()) {
            int optionCount = counts.get(outcomeOption);
            if (optionCount>maxOptionCount) {
                maxOption = outcomeOption;
                maxOptionCount = optionCount;
            }
        }

        return maxOption;

    }

    protected OutcomeOption matchOption(String optionDescription) {
        // see if the option matched exactly
        for (int i = 0;i<options.size();i++) {
            System.out.println(options.get(i).formatOption(i).toLowerCase() + " --- " + optionDescription.toLowerCase());
            if (options.get(i).formatOption(i+1).toLowerCase().contains(optionDescription.toLowerCase())) {
                return options.get(i);
            }
        }
        return NO_OPTION_SELECTED;
    }

    public abstract ConversationInterface<OpenAIChatMessageWrapper> generateConversation();
}
