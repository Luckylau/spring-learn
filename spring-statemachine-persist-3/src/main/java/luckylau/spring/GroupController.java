package luckylau.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author luckylau
 * @Date 2022/3/27
 */
@RestController
public class GroupController {
    @Autowired
    public GroupService groupService;

    @RequestMapping("/group/{groupId}/{event}")
    public boolean handle(@PathVariable("groupId") int groupId, @PathVariable("event") int event) {
        groupService.handleAction(groupId, event);
        return true;
    }

}
