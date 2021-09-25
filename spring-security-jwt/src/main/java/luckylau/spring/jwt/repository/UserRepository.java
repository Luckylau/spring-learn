package luckylau.spring.jwt.repository;


import luckylau.spring.jwt.entity.UserPO;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
public interface UserRepository extends PagingAndSortingRepository<UserPO, Long> {
    UserPO findByUsername(String username);
}
