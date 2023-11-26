package sj.noveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sj.noveling.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
