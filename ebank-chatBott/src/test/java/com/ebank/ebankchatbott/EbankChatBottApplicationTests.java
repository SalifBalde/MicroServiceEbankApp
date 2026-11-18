package com.ebank.ebankchatbott;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "discord.enabled=false"
})
class EbankChatBottApplicationTests {

    @Test
    void contextLoads() {
    }

}
