package luckylau.spring.controller;

import luckylau.spring.entity.HttpResult;
import luckylau.spring.entity.UserInfo;
import luckylau.spring.security.WebSecurityConfig;
import luckylau.spring.service.UserService;
import luckylau.spring.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {
    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public UserInfo getUserByUserId(@PathVariable String username) {
        return userService.findByusername(username);
    }

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public HttpResult<String> login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        HttpResult<String> rr = new HttpResult<String>();
        try {
            //通过 AuthenticationManager（默认实现为ProviderManager）的authenticate方法验证 Authentication 对象
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            //生成Token
            String token = jwtTokenUtils.createToken(authentication);
            //将Token写入到Http头部
            response.addHeader(WebSecurityConfig.AUTHORIZATION_HEADER, "Bearer " + token);
            rr.setCode(200);
            rr.setMessage("Success");
            rr.setData("Bearer " + token);
            return rr;
        } catch (BadCredentialsException authentication) {
            rr.setCode(401);
            rr.setMessage("Login failed");
            return rr;
        }
    }


}
