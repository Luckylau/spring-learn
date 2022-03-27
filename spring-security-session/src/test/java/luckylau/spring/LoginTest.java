package luckylau.spring;

import lombok.extern.slf4j.Slf4j;
import luckylau.spring.session.SessionApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = SessionApplication.class)
@Slf4j
public class LoginTest {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WebApplicationContext wac;

    //测试controller
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    /**
     * 服务启动起来测试
     */
    @Test
    public void login() {
        String body = "{\"username\":\"learn\",\"password\":\"learn\"}";
        HttpEntity<String> httpEntity = new HttpEntity<>(body);
        String result = restTemplate.postForObject("http://localhost:8070/user/login", httpEntity, String.class);
        log.info("result:{}", result);
    }

    @Test
    public void getUsername() throws Exception {
        String result = mockMvc.perform(get("/user"))
                .andReturn().getResponse().getContentAsString();
        log.info("result:{}", result);
    }
}
