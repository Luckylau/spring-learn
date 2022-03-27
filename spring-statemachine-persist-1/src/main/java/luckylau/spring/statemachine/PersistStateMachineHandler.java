package luckylau.spring.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.AbstractCompositeListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;

/**
 * @Author luckylau
 * @Date 2022/3/26
 */
@Component
@Slf4j
public class PersistStateMachineHandler extends StateMachineInterceptorAdapter<Status, Event> {

    private final CompositePersistStateChangeListener listeners = new CompositePersistStateChangeListener();
    @Autowired
    private StateMachineFactory<Status, Event> stateMachineFactory;

    public Mono<Void> handleEventWithStateReactively(Message<Event> event, Status status) {
        return Mono.defer(() -> {
            StateMachine<Status, Event> stateMachine = getInitStateMachine();
            return Mono.from(stateMachine.stopReactively())
                    .thenEmpty(Flux.fromIterable(stateMachine.getStateMachineAccessor().withAllRegions()).flatMap(region -> region.resetStateMachineReactively(new DefaultStateMachineContext<>(status, null
                            , null, null, null, null)))
                    )
                    .then(stateMachine.startReactively())
                    .thenMany(stateMachine.sendEvent(Mono.just(event)))
                    .then();
        });
    }

    public StateMachine<Status, Event> getInitStateMachine() {
        StateMachine<Status, Event> stateMachine = stateMachineFactory.getStateMachine();
        initStateMachine(stateMachine);
        return stateMachine;
    }

    public void addListener(PersistStateChangeListener listener) {
        listeners.register(listener);
    }

    public void initStateMachine(StateMachine<Status, Event> stateMachine) {
        stateMachine.getStateMachineAccessor().doWithAllRegions(statusEventStateMachineAccess -> statusEventStateMachineAccess.addStateMachineInterceptor(this));
        stateMachine.addStateListener(new StateMachineListenerAdapter<Status, Event>() {

            @Override
            public void stateChanged(State<Status, Event> from, State<Status, Event> to) {
                log.info("stateChanged: from : {}, to: {}", from.getId(), to.getId());
            }

            @Override
            public void eventNotAccepted(Message<Event> event) {
                log.info("eventNotAccepted :{}", event);
            }

            @Override
            public void stateContext(StateContext<Status, Event> stateContext) {
                if (stateContext.getStage() == StateContext.Stage.STATE_ENTRY) {
                    log.info("enter target: {}", stateContext.getTarget().getId());
                } else if (stateContext.getStage() == StateContext.Stage.STATE_EXIT) {
                    log.info("exit source: {}", stateContext.getSource().getId());
                } else if (stateContext.getStage() == StateContext.Stage.STATEMACHINE_START) {
                    log.info("Machine started");
                } else if (stateContext.getStage() == StateContext.Stage.STATEMACHINE_START) {
                    log.info("Machine stopped");
                }
            }
        });

    }


    @Override
    public void preStateChange(State<Status, Event> state, Message<Event> message, Transition<Status, Event> transition, StateMachine<Status, Event> stateMachine, StateMachine<Status, Event> rootStateMachine) {
        listeners.onPersist(state, message, transition, stateMachine);
    }

    public interface PersistStateChangeListener {
        /**
         * Called when state needs tp be persisted
         *
         * @param state
         * @param message
         * @param transition
         * @param stateMachine
         */
        void onPersist(State<Status, Event> state, Message<Event> message, Transition<Status, Event> transition,
                       StateMachine<Status, Event> stateMachine);
    }

    private static class CompositePersistStateChangeListener extends AbstractCompositeListener<PersistStateChangeListener> implements PersistStateChangeListener {

        @Override
        public void onPersist(State<Status, Event> state, Message<Event> message, Transition<Status, Event> transition, StateMachine<Status, Event> stateMachine) {
            for (Iterator<PersistStateChangeListener> iterator = getListeners().reverse(); iterator.hasNext(); ) {
                PersistStateChangeListener listener = iterator.next();
                listener.onPersist(state, message, transition, stateMachine);
            }
        }
    }

}
