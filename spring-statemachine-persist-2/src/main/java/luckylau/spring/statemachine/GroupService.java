package luckylau.spring.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Author luckylau
 * @Date 2022/3/26
 */
@Service
@Slf4j
public class GroupService {

    @Autowired
    private StateMachineFactory<Status, Event> stateMachineFactory;

    @Autowired
    private StateMachineRuntimePersister<Status, Event, String> statusEventStringStateMachineRuntimePersister;


    public void handleAction(int groupId, int event) {
        String machineId = "machine:" + groupId;
        handleEventWithStateReactively(MessageBuilder.withPayload(Event.valueOf(event)).build(), machineId).subscribe();
    }

    public Mono<Void> handleEventWithStateReactively(Message<Event> event, String machineId) {
        return Mono.defer(() -> {
            StateMachine<Status, Event> stateMachine = getInitStateMachine(machineId);
            return Mono.from(stateMachine.stopReactively())
                    .thenEmpty(Flux.fromIterable(stateMachine.getStateMachineAccessor().withAllRegions()).flatMap(region -> {
                                try {
                                    StateMachineContext<Status, Event> context = statusEventStringStateMachineRuntimePersister.read(machineId);
                                    if (context != null) {
                                        log.info("redis context: {}", context.getState());
                                        return region.resetStateMachineReactively(context);
                                    }
                                } catch (Exception e) {
                                    log.error("resetStateMachineReactively error:{}", e.getMessage(), e);
                                }
                                return region.resetStateMachineReactively(new DefaultStateMachineContext<>(Status.PENDING_APPROVAL, null, null, null, null, machineId));
                            }
                    )
                            .then(stateMachine.startReactively())
                            .thenMany(stateMachine.sendEvent(Mono.just(event)))
                            .then());
        });
    }

    public StateMachine<Status, Event> getInitStateMachine(String machineId) {
        StateMachine<Status, Event> stateMachine = stateMachineFactory.getStateMachine(machineId);
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
        return stateMachine;
    }
}
