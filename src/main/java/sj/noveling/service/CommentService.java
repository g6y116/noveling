package sj.noveling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sj.noveling.entity.Chapter;
import sj.noveling.entity.Comment;
import sj.noveling.entity.Member;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.form.AddCommentForm;
import sj.noveling.form.SetCommentForm;
import sj.noveling.repository.ChapterRepository;
import sj.noveling.repository.CommentRepository;
import sj.noveling.repository.MemberRepository;
import sj.noveling.repository.NovelRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;

    // 댓글 등록
    public Comment addComment(AddCommentForm form, Long memberId) {

        Optional<Chapter> chapter = chapterRepository.findById(form.getChapterId());
        Optional<Member> member = memberRepository.findById(memberId);

        if (chapter.isPresent() && member.isPresent()) {
            return commentRepository.save(new Comment(
                form.getContent(),
                chapter.get(),
                member.get()
            ));
        } else {
            throw new DataNotFoundException("회차가 존재하지 않습니다.");
        }
    }

    // 댓글 수정
    public Comment setComment(SetCommentForm form) {

        Optional<Comment> comment = commentRepository.findById(form.getId());

        if (comment.isPresent()) {
            comment.get().setContent(form.getContent());
            comment.get().setModifyDate(LocalDateTime.now());
            return commentRepository.save(comment.get());
        } else {
            throw new DataNotFoundException("댓글이 존재하지 않습니다.");
        }
    }

    // 댓글 삭제
    public Boolean removeComment(Long commentId) {

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
            return true;
        } else {
            return false;
        }
    }

    public Long getChapterId(Long commentId) {

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isPresent()) {
            return comment.get().getChapter().getId();
        } else {
            throw new DataNotFoundException("댓글이 속한 회차가 존재하지 않습니다.");
        }
    }
}
