package com.ebank.ebankchatbott.controllers;


import com.ebank.ebankchatbott.agents.EbankAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class chatbotController {

    private final EbankAgent ebankAgent;

    public chatbotController(EbankAgent ebankAgent) {
        this.ebankAgent = ebankAgent;
    }

    @GetMapping("/chat")
    public  String chat(@RequestParam(name = "query", defaultValue = "Bonjour") String query,
                        @RequestParam String conversationId){
        return this.ebankAgent.chat(query,conversationId);
    }
}
