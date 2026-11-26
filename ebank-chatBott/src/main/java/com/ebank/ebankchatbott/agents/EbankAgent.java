
package com.ebank.ebankchatbott.agents;

import java.util.List;

import com.ebank.ebankchatbott.config.McpClientFactory;
import io.modelcontextprotocol.client.McpSyncClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EbankAgent {

    private final ChatClient chatClient;
    private final McpClientFactory mcpClientFactory;

    public EbankAgent(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            McpClientFactory mcpClientFactory) {

        this.chatClient = chatClientBuilder
                .defaultSystem("""
        Tu es un assistant bancaire sécurisé.

        Tu réponds aux questions des utilisateurs concernant
        les clients et les comptes bancaires uniquement
        à partir des informations fournies par les outils.

        RÈGLES DE SÉCURITÉ :

        1. Lorsqu'un utilisateur CUSTOMER demande à consulter
           ses propres comptes, tu DOIS appeler l'outil myAccounts.

        2. Ne demande jamais à un client son adresse e-mail,
           son identifiant client ou son numéro de compte
           pour retrouver ses propres comptes.

        3. N'utilise jamais allAccount pour répondre à un client
           qui demande ses propres comptes.

        4. Ne demande jamais à l'utilisateur de fournir
           son identité pour contourner les contrôles de sécurité.

        5. Ne prétends jamais avoir consulté des comptes
           si aucun outil n'a été appelé avec succès.

        6. Si un outil échoue ou ne fournit aucune information,
           explique simplement que les comptes ne sont pas accessibles.

        7. Ne divulgue jamais les comptes d'un autre client.

        Tu dois respecter les autorisations définies
        par les services bancaires.
        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();

        this.mcpClientFactory = mcpClientFactory;
    }

    public String chat(
            String query,
            String conversationId,
            String accessToken) {

        McpSyncClient customerClient = null;
        McpSyncClient accountClient = null;

        try {
            customerClient =
                    mcpClientFactory.createCustomerClient(accessToken);

            accountClient =
                    mcpClientFactory.createAccountClient(accessToken);

            var provider = SyncMcpToolCallbackProvider.builder()
                    .mcpClients(List.of(customerClient, accountClient))
                    .build();

            return chatClient.prompt()
                    .user(query)
                    .toolCallbacks(provider.getToolCallbacks())
                    .advisors(advisor -> advisor.param(
                            ChatMemory.CONVERSATION_ID,
                            conversationId
                    ))
                    .call()
                    .content();

        } finally {
            closeClient(accountClient);
            closeClient(customerClient);
        }
    }

    private void closeClient(McpSyncClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println(
                        "Erreur fermeture client MCP : " + e.getMessage()
                );
            }
        }
    }
}
