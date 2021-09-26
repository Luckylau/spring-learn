package luckylau.spring.session.security;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;


/**
 * @Author liuJun
 * @Date 2019/2/27
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class SpringSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private static final String REDIS_SESSION_NAME_SPACE = "default_session";
    private static final Integer MAX_INACTIVE_INTERVAL = 60 * 60 * 12;
    @Autowired
    @Qualifier("sessionRedisTemplate")
    RedisTemplate<Object, Object> sessionRedisTemplate;
    @Autowired
    private DefaultAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private DefaultAuthenticationProvider authenticationProvider;
    @Autowired
    private DefaultAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private DefaultAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private DefaultLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf()
                .disable().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests().antMatchers("/system/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/user/login").permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        http.addFilterBefore(defaultAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterBefore(concurrentSessionFilter(), ConcurrentSessionFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter() {
        return new ConcurrentSessionFilter(sessionRegistry(), sessionInformationExpiredStrategy());
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new SimpleRedirectSessionInformationExpiredStrategy("/user/login");
    }


    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository());
    }

    @Bean
    @Primary
    public FindByIndexNameSessionRepository sessionRepository() {
        RedisOperationsSessionRepository repository = new RedisOperationsSessionRepository(sessionRedisTemplate);
        repository.setRedisKeyNamespace(REDIS_SESSION_NAME_SPACE);
        repository.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        repository.setDefaultMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
        return repository;
    }

    @Bean
    public DefaultAuthenticationFilter defaultAuthenticationFilter() {
        DefaultAuthenticationFilter ivrAuthenticationFilter = new DefaultAuthenticationFilter("/user/login");
        ivrAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        ivrAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        ivrAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return ivrAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
