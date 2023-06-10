package luckylau.spring.session.controller;

import luckylau.spring.session.security.DefaultAuthenticationToken;
import luckylau.spring.session.service.UserService;
import luckylau.spring.session.vo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author luckylau
 * @Date 2021/9/26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public HttpResult getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return HttpResult.fail("NOT FOUND");
        }
        return HttpResult.success(((DefaultAuthenticationToken) authentication).getUser());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public HttpResult getUserList() {
        return HttpResult.success(userService.list());
    }
}
