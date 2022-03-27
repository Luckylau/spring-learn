package luckylau.spring.session.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;


/**
 * @Author liuJun
 * @Date 2019/2/27
 */
@Configuration
@EnableWebSecurity
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 12 * 60 * 60)
public class SpringSecurityConfigurer extends WebSecurityConfigurerAdapter {
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

    @Autowired
    private RedisIndexedSessionRepository redisIndexedSessionRepository;

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
        return new SpringSessionBackedSessionRegistry(redisIndexedSessionRepository);
    }

    @Bean
    public DefaultAuthenticationFilter defaultAuthenticationFilter() {
        DefaultAuthenticationFilter authenticationFilter = new DefaultAuthenticationFilter("/user/login");
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        return authenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
