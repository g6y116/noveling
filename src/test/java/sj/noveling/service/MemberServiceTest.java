package sj.noveling.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sj.noveling.entity.Member;
import sj.noveling.exception.DataExistsException;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.form.JoinForm;
import sj.noveling.form.LoginForm;
import sj.noveling.repository.ChapterRepository;
import sj.noveling.repository.CommentRepository;
import sj.noveling.repository.MemberRepository;
import sj.noveling.repository.NovelRepository;
import sj.noveling.type.Grade;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

/*
    import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
    import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

    assertThat().isEqualTo();
    assertThat().isNull()
    assertThat().isTrue();
    assertThat().containsExactly();
    assertThat().extracting();
    assertThatThrownBy(() -> { METHOD() }).isInstanceOf(Exception.class).hasMessageContaining("");
 */

@SpringBootTest
@Slf4j
@Transactional(readOnly = false)
@Rollback(value = true)
class MemberServiceTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    MemberService memberService;

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

        memberRepository.save(new Member("회원A", "qwer", "qwer"));
    }

    @Test
    void join() {
        //given
        JoinForm joinForm1 = new JoinForm("회원A", "asdf", "asdf");
        JoinForm joinForm2 = new JoinForm("회원B", "qwer", "qwer");
        JoinForm joinForm3 = new JoinForm("회원C", "asdf", "asdf");

        //when
        Member member = memberService.join(joinForm3);

        //then
        assertThatThrownBy(() -> { memberService.join(joinForm1); })
                .isInstanceOf(DataExistsException.class).hasMessageContaining("이미 존재하는 필명입니다.");

        assertThatThrownBy(() -> { memberService.join(joinForm2); })
                .isInstanceOf(DataExistsException.class).hasMessageContaining("이미 존재하는 아이디입니다.");

        assertThat(member.getName()).isEqualTo("회원C");
        assertThat(member.getLoginId()).isEqualTo("asdf");
        assertThat(member.getLoginPw()).isEqualTo("asdf");
    }

    @Test
    void login() {
        //given
        LoginForm loginForm1 = new LoginForm("asdf", "asdf");
        LoginForm loginForm2 = new LoginForm("qwer", "qwer");

        //when

        //then
        assertThatThrownBy(() -> { memberService.login(loginForm1); })
                .isInstanceOf(DataNotFoundException.class).hasMessageContaining("로그인 정보가 맞지 않습니다.");

        Member member = memberService.login(loginForm2);
        assertThat(member.getName()).isEqualTo("회원A");
        assertThat(member.getLoginId()).isEqualTo("qwer");
        assertThat(member.getLoginPw()).isEqualTo("qwer");
    }

    @Test
    void removeMember() {
        //given
        Member member = memberRepository.save(new Member("회원B", "asdf", "asdf"));

        //when

        //then
        assertThat(memberService.removeMember(9999L)).isFalse();
        assertThat(memberService.removeMember(member.getId())).isTrue();
    }
}