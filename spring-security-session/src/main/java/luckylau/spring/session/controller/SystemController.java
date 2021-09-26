package luckylau.spring.session.controller;

import luckylau.spring.session.entity.UserPO;
import luckylau.spring.session.security.DefaultSessionManagement;
import luckylau.spring.session.service.UserService;
import luckylau.spring.session.vo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author luckylau
 * @Date 2021/9/26
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultSessionManagement defaultSessionManagement;

    @RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
    public HttpResult delete(@PathVariable Long id) {
        UserPO userPO = userService.findById(id);
        if (userPO == null) {
            return HttpResult.fail("用户不存在");
        }
        userService.deleteUser(id);
        //并强制下线
        defaultSessionManagement.forceUserLogOut(userPO.getUsername());
        return HttpResult.success(null);
    }
}
