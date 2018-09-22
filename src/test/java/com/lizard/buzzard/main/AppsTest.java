package com.lizard.buzzard.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(/*webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,*/ classes = Apps.class)
public class AppsTest {
    @Test
    public void contextLoads() {
//        System.out.println("============> main test passed");
    }
}
