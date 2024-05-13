package study.myrestfulservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.myrestfulservice.bean.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
