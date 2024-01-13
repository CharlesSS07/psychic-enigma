package com.psychicenigma;

import com.theokanning.openai.service.OpenAiService;

public class Settings {

    private static final String OPENAI_TOKEN = System.getenv("OPENAI_TOKEN");
    public static final OpenAiService OPEN_AI_SERVICE = new OpenAiService(OPENAI_TOKEN);

}
