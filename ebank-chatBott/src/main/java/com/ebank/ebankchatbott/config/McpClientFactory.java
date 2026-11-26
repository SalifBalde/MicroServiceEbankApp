
package com.ebank.ebankchatbott.config;

import java.time.Duration;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class McpClientFactory {

    @Value("${app.mcp.customer.base-url}")
    private String customerBaseUrl;

    @Value("${app.mcp.account.base-url}")
    private String accountBaseUrl;

    public McpSyncClient createCustomerClient(String accessToken) {
        return createClient(customerBaseUrl + "/mcp", accessToken);
    }

    public McpSyncClient createAccountClient(String accessToken) {
        return createClient(accountBaseUrl + "/mcp", accessToken);
    }

    private McpSyncClient createClient(
            String endpoint,
            String accessToken) {

        var transport = HttpClientStreamableHttpTransport
                .builder(endpoint)
                .openConnectionOnStartup(false)
                .httpRequestCustomizer((requestBuilder, method, uri, body, context) ->
                        requestBuilder.header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                )
                .build();

        McpSyncClient client = McpClient.sync(transport)
                .requestTimeout(Duration.ofSeconds(30))
                .initializationTimeout(Duration.ofSeconds(30))
                .build();

        client.initialize();

        return client;
    }
}
