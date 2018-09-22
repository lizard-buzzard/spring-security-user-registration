package com.lizard.buzzard.web.controller.serurity;

import com.lizard.buzzard.web.controller.AbstractControllerTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

public class SecurityTest extends AbstractControllerTest {

    private static MockMvc mvc;

    @BeforeClass
    public static void initTests() {
        mvc = getMvc();
    }

    @Test
    public void test() {
        System.out.println("======> test is passed");
    }
}
