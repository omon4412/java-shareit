package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void runApplication() {
        ShareItApp.main(new String[]{});
    }

}
