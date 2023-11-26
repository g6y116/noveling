package sj.noveling.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sj.noveling.repository.ChapterRepository;
import sj.noveling.repository.CommentRepository;
import sj.noveling.repository.MemberRepository;
import sj.noveling.repository.NovelRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional(readOnly = false)
@Rollback(value = true)
class ChapterServiceTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    ChapterService chapterService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NovelRepository novelRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    void getChapters() {
    }

    @Test
    void getChapter() {
    }

    @Test
    void addChapter() {
    }

    @Test
    void setChapter() {
    }

    @Test
    void removeChapter() {
    }
}