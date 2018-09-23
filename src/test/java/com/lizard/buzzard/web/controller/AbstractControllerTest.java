package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.Apps;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Apps.class)
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {
    @Autowired
    private static MockMvc mvc;

    public static MockMvc getMvc() {
        return mvc;
    }
}
