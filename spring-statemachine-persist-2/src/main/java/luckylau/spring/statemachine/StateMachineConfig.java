package luckylau.spring.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.EnumSet;

/**
 * @Author luckylau
 * @Date 2022/4/11
 */
@EnableStateMachineFactory
@Configuration
@Slf4j
public class StateMachineConfig extends StateMachineConfigurerAdapter<Status, Event> {

    @Autowired
    private RedisStateMachineRepository redisStateMachineRepository;

    @Bean
    public StateMachineRuntimePersister<Status, Event, String> statusEventStringStateMachineRuntimePersister() {
        return new RedisPersistingStateMachineInterceptor<>(redisStateMachineRepository);
    }

    @Override
    public void configure(StateMachineStateConfigurer<Status, Event> states) throws Exception {
        states.withStates().initial(Status.PENDING_APPROVAL).choice(Status.CHOICE).states(EnumSet.allOf(Status.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<Status, Event> config) throws Exception {
        config.withPersistence().runtimePersister(statusEventStringStateMachineRuntimePersister());
        //如果配置自动启动的话，会覆盖之前保存的数据
        //原因是初始化启动时候，StateMachineRuntimePersister postStateChange会执行保存状态
        config.withConfiguration().autoStartup(false);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Status, Event> transitions) throws Exception {
        configTransitions(transitions);
    }

    private void configTransitions(StateMachineTransitionConfigurer<Status, Event> transitions) throws Exception {
        transitions.withExternal()
                .source(Status.PENDING_APPROVAL).target(Status.PARTIAL_APPROVED).event(Event.APPROVE).action(new FooAction(), new ErrorHandleAction())
                .and()
                .withExternal()
                .source(Status.PENDING_APPROVAL).target(Status.REJECTED).event(Event.REJECT)
                .and()
                .withExternal()
                .source(Status.PARTIAL_APPROVED).target(Status.CHOICE).event(Event.APPROVE)
                .and()
                .withExternal()
                .source(Status.PARTIAL_APPROVED).target(Status.REJECTED).event(Event.REJECT)
                .and()
                .withChoice()
                .source(Status.CHOICE)
                .first(Status.PENDING_ADMIN_APPROVE, new Check())
                .last(Status.APPROVED)
                .and()
                .withExternal()
                .source(Status.PENDING_ADMIN_APPROVE).target(Status.PENDING_ADMIN_APPROVE_CONFIRM).event(Event.APPROVE)
                .and()
                .withExternal()
                .source(Status.PENDING_ADMIN_APPROVE).target(Status.PENDING_ADMIN_REJECT_CONFIRM).event(Event.REJECT)
                .and()
                .withExternal()
                .source(Status.PENDING_ADMIN_REJECT_CONFIRM).target(Status.PENDING_ADMIN_APPROVE).event(Event.REJECT)
                .and()
                .withExternal()
                .source(Status.PENDING_ADMIN_APPROVE_CONFIRM).target(Status.APPROVED).event(Event.APPROVE)
                .and()
                .withExternal()
                .source(Status.PENDING_ADMIN_APPROVE_CONFIRM).target(Status.PENDING_ADMIN_APPROVE).event(Event.REJECT)
                .and()
                .withExternal()
                .source(Status.PENDING_ADMIN_REJECT_CONFIRM).target(Status.REJECTED).event(Event.APPROVE);
    }


    private static class FooAction implements Action<Status, Event> {

        @Override
        public void execute(StateContext<Status, Event> context) {
            log.info("action exe ............");
        }
    }

    private static class ErrorHandleAction implements Action<Status, Event> {

        @Override
        public void execute(StateContext<Status, Event> context) {
            log.info("errorHandleAction exe:{}", context.getException().getMessage());

        }
    }

    private static class Check implements Guard<Status, Event> {

        @Override
        public boolean evaluate(StateContext<Status, Event> context) {
            Group group = context.getMessage().getHeaders().get("group", Group.class);
            return group != null && group.isAdvance();
        }
    }
}
