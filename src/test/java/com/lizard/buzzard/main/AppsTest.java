package com.lizard.buzzard.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(/*webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,*/ classes = Apps.class)
public class AppsTest {
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Test
//    public void shouldStartEurekaServer() {
//        ResponseEntity<String> entity = this.testRestTemplate.getForEntity(
//                "http://localhost:" + this.port + "/eureka/apps", String.class);
//        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }

    @Test
    public void contextLoads() {
        System.out.println("============> main test passed");
    }
}
