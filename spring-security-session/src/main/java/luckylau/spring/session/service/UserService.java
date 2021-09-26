package luckylau.spring.session.service;

import luckylau.spring.session.entity.UserPO;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
public interface UserService {
    UserPO findByUsername(String username);

    void deleteUser(Long id);

    UserPO findById(Long id);
}
