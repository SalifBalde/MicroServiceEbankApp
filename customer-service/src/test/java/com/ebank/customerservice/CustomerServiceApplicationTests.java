package com.ebank.customerservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomerServiceApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void testAddition() {
        int resultat = 100 + 50;

        assertEquals(150, resultat);
    }

}
