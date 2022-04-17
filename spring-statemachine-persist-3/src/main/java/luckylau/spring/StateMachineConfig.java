package luckylau.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;

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
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public StateMachinePersist<Status, Event, String> statusEventStringStateMachinePersist(RedisConnectionFactory redisConnectionFactory) {
        RedisStateMachineContextRepository<Status, Event> redisStateMachineContextRepository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        return new RepositoryStateMachinePersist<>(redisStateMachineContextRepository);
    }

    @Bean
    public StateMachinePersister<Status, Event, String> stringStateMachinePersister(StateMachinePersist<Status, Event, String> stateMachinePersist) {
        return new RedisStateMachinePersister<>(stateMachinePersist);
    }


    @Override
    public void configure(StateMachineStateConfigurer<Status, Event> states) throws Exception {
        states.withStates().initial(Status.PENDING_APPROVAL).choice(Status.CHOICE).states(EnumSet.allOf(Status.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<Status, Event> config) throws Exception {
        config.withConfiguration().autoStartup(true);
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
