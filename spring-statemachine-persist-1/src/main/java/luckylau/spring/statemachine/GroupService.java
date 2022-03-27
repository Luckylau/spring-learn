package luckylau.spring.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

/**
 * @Author luckylau
 * @Date 2022/3/26
 */
@Service
@Slf4j
public class GroupService implements InitializingBean, PersistStateMachineHandler.PersistStateChangeListener {

    @Autowired
    private PersistStateMachineHandler persistStateMachineHandler;

    @Autowired
    private GroupRepository groupRepository;


    public void handleAction(int groupId, int event) {
        Group group = groupRepository.findGroupId(groupId);
        if (group == null) {
            group = groupRepository.create(groupId);
        }
        persistStateMachineHandler.handleEventWithStateReactively(MessageBuilder.withPayload(Event.valueOf(event)).setHeader("group", group).build(), Status.valueOf(group.getStatus())).subscribe();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        persistStateMachineHandler.addListener(this);
    }

    @Override
    public void onPersist(State<Status, Event> state, Message<Event> message, Transition<Status, Event> transition, StateMachine<Status, Event> stateMachine) {
        if (message != null) {
            Group group = message.getHeaders().get("group", Group.class);
            if (group != null) {
                log.info("db update: update group set status = {} where groupId = {}", state.getId().getStatusCode(), group.getGroupId());
            }
        }
    }
}
