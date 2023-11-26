package sj.noveling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sj.noveling.entity.Member;
import sj.noveling.exception.DataExistsException;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.form.JoinForm;
import sj.noveling.form.LoginForm;
import sj.noveling.repository.ChapterRepository;
import sj.noveling.repository.CommentRepository;
import sj.noveling.repository.MemberRepository;
import sj.noveling.repository.NovelRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;

    public Member join(JoinForm form) {
        if (memberRepository.existsByName(form.getName())) { // 필명 중복 여부
            throw new DataExistsException("이미 존재하는 필명입니다.");
        }

        if (memberRepository.existsByLoginId(form.getLoginId())) { // 아이디 중복 여부
            throw new DataExistsException("이미 존재하는 아이디입니다.");
        }

        return memberRepository.save(new Member(form.getName(), form.getLoginId(), form.getLoginPw()));
    }

    public Member login(LoginForm form) {
        Optional<Member> member = memberRepository.findByLoginIdAndLoginPw(form.getLoginId(), form.getLoginPw());

        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("로그인 정보가 맞지 않습니다.");
        }
    }

    // 회원 삭제
    public Boolean removeMember(Long memberId) {

        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            memberRepository.delete(member.get());
            return true;
        } else {
            return false;
        }
    }
}
