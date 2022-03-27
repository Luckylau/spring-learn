package luckylau.spring.session.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Author luckylau
 * @Date 2021/9/25
 */
@Configuration
public class RedisConfig {
// 暂不支持更换GenericJackson2JsonRedisSerializer
//    @Bean("springSessionDefaultRedisSerializer")
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
//        return new GenericJackson2JsonRedisSerializer(objectMapper);
//    }
}
