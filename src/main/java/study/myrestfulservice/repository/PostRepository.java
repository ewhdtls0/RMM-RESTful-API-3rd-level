package study.myrestfulservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.myrestfulservice.bean.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
