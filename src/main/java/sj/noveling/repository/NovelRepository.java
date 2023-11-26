package sj.noveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sj.noveling.entity.Member;
import sj.noveling.entity.Novel;

import java.util.List;

public interface NovelRepository extends JpaRepository<Novel, Long> {
    List<Novel> findByMember(Member member); // 내 작품 조회
    Novel findByTitle(String title);
}
