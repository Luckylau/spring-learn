package luckylau.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import luckylau.spring.session.SessionApplication;
import luckylau.spring.session.security.DefaultAuthenticationFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = SessionApplication.class)
@Slf4j
public class LoginTest {

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private DefaultAuthenticationFilter defaultAuthenticationFilter;

    //测试controller
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(defaultAuthenticationFilter, "/user/login").build();
    }


    /**
     * 服务启动起来测试
     */
    @Test
    @Order(1)
    public void login() throws Exception {
        String body = "{\"username\":\"learn\",\"password\":\"learn\"}";
        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        log.info("result:{}", result);
        ObjectMapper mapper = new ObjectMapper();
        Assertions.assertEquals(200, mapper.readTree(result).get("code").asInt());
    }

    @Test
    @Order(2)
    public void getUsername() throws Exception {
        String result = mockMvc.perform(get("/user"))
                .andReturn().getResponse().getContentAsString();
        log.info("getUsername:{}", result);
        ObjectMapper mapper = new ObjectMapper();
        Assertions.assertEquals("learn", mapper.readTree(result).get("data").get("username").asText());
    }
}
