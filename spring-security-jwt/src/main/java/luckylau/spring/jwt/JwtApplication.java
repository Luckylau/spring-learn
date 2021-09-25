package luckylau.spring.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author luckylau
 * @Date 2021/9/25
 */
@SpringBootApplication(scanBasePackages = "luckylau.spring.jwt")
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }
}
