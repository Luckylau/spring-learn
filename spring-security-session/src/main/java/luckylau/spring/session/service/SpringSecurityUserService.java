package luckylau.spring.session.service;


import luckylau.spring.session.entity.UserPO;
import luckylau.spring.session.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@Service
public class SpringSecurityUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserPO findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserPO findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
