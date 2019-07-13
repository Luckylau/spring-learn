package luckylau.spring.service;

import luckylau.spring.entity.UserInfo;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
public interface UserService {
    UserInfo findByusername(String username);
}
