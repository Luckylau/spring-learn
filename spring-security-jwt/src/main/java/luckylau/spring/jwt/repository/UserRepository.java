package luckylau.spring.jwt.repository;


import luckylau.spring.jwt.entity.UserPO;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserPO, Long> {
    UserPO findByUsername(String username);
}
