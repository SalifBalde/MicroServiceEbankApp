//package com.ebank.ebankchatbott.discord;
//
//import com.ebank.ebankchatbott.agents.EbankAgent;
//import com.zgamelogic.discord.annotations.DiscordController;
//import com.zgamelogic.discord.annotations.DiscordMapping;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//
//@DiscordController
//public class DiscordBot {
//
//    private EbankAgent ebankAgent;
//
//    public DiscordBot(EbankAgent ebankAgent) {
//        this.ebankAgent = ebankAgent;
//    }
//
//
//    @DiscordMapping
//    private void perform(MessageReceivedEvent event){
//        if (event.getAuthor().isBot()) return;
//
//        // Une mémoire de conversation par utilisateur
//        String conversationId = event.getAuthor().getId();
//        String query = event.getMessage().getContentRaw();
//        String response = this.ebankAgent.chat(query, conversationId);
//        event.getChannel().sendMessage(response).queue();
//
//    }
//}
