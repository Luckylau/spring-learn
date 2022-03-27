package luckylau.spring.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟数据库存储
 *
 * @Author luckylau
 * @Date 2022/3/26
 */
@Service
@Slf4j
public class GroupRepository {
    private static Map<Integer, Group> store = new ConcurrentHashMap<>();

    static {
        store.put(1, new Group.GroupBuilder().groupId(1).isAdvance(true).status(Status.PENDING_APPROVAL.getStatusCode()).build());
    }

    public Group create(int groupId) {
        Group group = new Group.GroupBuilder().groupId(groupId).isAdvance(true).status(Status.PENDING_APPROVAL.getStatusCode()).build();
        store.putIfAbsent(groupId, group);
        return store.get(groupId);
    }

    public Group findGroupId(int groupId) {
        return store.get(groupId);
    }

    public void update(int groupId, int status) {
        Group group = store.get(groupId);
        group.setStatus(status);
    }
}
