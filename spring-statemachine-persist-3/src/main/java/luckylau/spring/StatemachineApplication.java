package luckylau.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @Author luckylau
 * @Date 2022/4/11
 */
@SpringBootApplication
@EnableRedisRepositories("org.springframework.statemachine.data.redis")
public class StatemachineApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatemachineApplication.class, args);
    }
}
