package luckylau.spring.jwt.service;

import luckylau.spring.jwt.entity.UserInfo;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
public interface UserService {
    UserInfo findByUsername(String username);
}
