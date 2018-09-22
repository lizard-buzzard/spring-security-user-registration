package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.Apps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * NOTE: in opposite to integration test with @SpringBootTest and @AutoConfigureMockMvc these annotations start pointed controller only.
 * SEE: errors could occur if do not respect conditions mentioned in
 * https://stackoverflow.com/questions/43515279/error-unable-to-find-springbootconfiguration-when-doing-webmvctest-for-spring/43517967
 * https://stackoverflow.com/questions/41057602/spring-boot-1-4-testing-configuration-error-found-multiple-declarations-of-b
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers={RegisterController.class})
@ContextConfiguration(classes=Apps.class)
public class LoginTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test1() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

}
