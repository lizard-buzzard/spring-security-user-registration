package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.Apps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * NOTE: @SpringBootTest is good for mocking an integration tests.
 * SEE: https://www.baeldung.com/spring-boot-testing
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Apps.class)
@AutoConfigureMockMvc
public class LoginTest1 {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test1() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }
}
