package luckylau.spring.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = JwtApplication.class)
@Slf4j
public class LoginTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    @Order(1)
    public void login() throws Exception {
        String result = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "learn").param("password", "learn"))
                .andReturn().getResponse().getContentAsString();
        log.info("result:{}", result);
        ObjectMapper mapper = new ObjectMapper();
        Assertions.assertEquals(200, mapper.readTree(result).get("code").asInt());
    }

    @Test
    @Order(2)
    public void getUsername() throws Exception {
        String result = mockMvc.perform(get("/user/learn"))
                .andReturn().getResponse().getContentAsString();
        log.info("result:{}", result);
        ObjectMapper mapper = new ObjectMapper();
        Assertions.assertEquals("learn", mapper.readTree(result).get("name").asText());

    }






}
