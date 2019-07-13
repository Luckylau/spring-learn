package luckylau.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = "luckylau.spring")
public class JwtApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JwtApp.class, args);
    }
}
