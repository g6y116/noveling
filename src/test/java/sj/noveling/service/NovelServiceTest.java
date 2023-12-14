package sj.noveling.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sj.noveling.dto.NovelSimpleDto;
import sj.noveling.entity.Member;
import sj.noveling.entity.Novel;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.form.AddNovelForm;
import sj.noveling.repository.*;
import sj.noveling.type.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional(readOnly = false)
@Rollback(value = true)
class NovelServiceTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    NovelService novelService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NovelRepository novelRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    QueryDslRepository queryDslRepository;

    Member testMember;
    List<Novel> testNovels;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        testMember = memberRepository.save(new Member("회원A", "qwer", "qwer"));
        testNovels = new ArrayList<>();
        testNovels.add(novelRepository.save(new Novel("소설 제목1", "소설 설명1", null, Genre.FANTASY, testMember)));
        testNovels.add(novelRepository.save(new Novel("소설 제목2", "소설 설명2", null, Genre.FANTASY, testMember)));
        testNovels.add(novelRepository.save(new Novel("소설 제목3", "소설 설명3", null, Genre.FANTASY, testMember)));
        testNovels.add(novelRepository.save(new Novel("소설 제목4", "소설 설명4", null, Genre.CHINESE, testMember)));
    }

    @Test
    @Disabled
    void getNovels() {
        //given

        //when
        Page<NovelSimpleDto> novels0 = novelService.getNovels(0, null, null);
        Page<NovelSimpleDto> novels1 = novelService.getNovels(0, null, Genre.FANTASY);
        Page<NovelSimpleDto> novels2 = novelService.getNovels(0, "제목1", null);
        Page<NovelSimpleDto> novels3 = novelService.getNovels(0, "제목1", Genre.CHINESE);

        novels0.toList().forEach( it -> { log.info("novels0 = {}", it.getTitle()); });
        novels1.toList().forEach( it -> { log.info("novels1 = {}", it.getTitle()); });
        novels2.toList().forEach( it -> { log.info("novels2 = {}", it.getTitle()); });
        novels3.toList().forEach( it -> { log.info("novels3 = {}", it.getTitle()); });

        //then
        List<NovelSimpleDto> content = novels0.getContent();//조회된 데이터
        assertThat(content.size()).isEqualTo(4); //조회된 데이터 수
        assertThat(novels0.getTotalElements()).isEqualTo(4); //전체 데이터 수
        assertThat(novels0.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(novels0.getTotalPages()).isEqualTo(1); //전체 페이지 번호
        assertThat(novels0.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(novels0.hasNext()).isFalse(); //다음 페이지가 없는가?
    }

    @Test
    void getNovel() {
        //given
        Novel novel = novelRepository
            .save(new Novel("소설 제목", "소설 설명", null, Genre.FANTASY, testMember));

        //when

        //then
        assertThatThrownBy(() -> { novelService.getNovel(9999L); })
                .isInstanceOf(DataNotFoundException.class).hasMessageContaining("작품이 존재하지 않습니다.");
        assertThat(novel.getTitle()).isEqualTo("소설 제목");
        assertThat(novel.getDescription()).isEqualTo("소설 설명");
    }

    @Test
    @Disabled
    void getBestNovels() {
        //given

        //when
        queryDslRepository.bestNovels().forEach( it -> { log.info("novel title = {}", it.getTitle()); });

        //then
    }

    @Test
    void addNovel() {
        //given
        AddNovelForm addNovelForm = new AddNovelForm("제목111", "설명111", null, Genre.FANTASY);

        //when
        novelService.addNovel(addNovelForm, testMember);
        Novel novel = novelRepository.findByTitle("제목111");

        //then
        assertThat(novel).isNotNull();
    }

    @Test
    void setNovel() {
        //given
        AddNovelForm addNovelForm = new AddNovelForm("제목111", "설명111", null, Genre.FANTASY);

        //when
        novelService.addNovel(addNovelForm, testMember);
        Novel novel = novelRepository.findByTitle("제목111");
        novel.setDescription("설명222");

        //then
        assertThat(novel.getDescription()).isEqualTo("설명222");
    }

    @Test
    void removeNovel() {
        //given
        AddNovelForm addNovelForm = new AddNovelForm("제목111", "설명111", null, Genre.FANTASY);

        //when
        Novel novel = novelService.addNovel(addNovelForm, testMember);

        //then
        assertThat(novel).isNotNull();

        //when
        novelService.removeNovel(novel.getId());

        //then
        assertThat(novelRepository.findByTitle("제목111")).isNull();
    }
}