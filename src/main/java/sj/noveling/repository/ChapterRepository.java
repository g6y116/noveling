package sj.noveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sj.noveling.entity.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
