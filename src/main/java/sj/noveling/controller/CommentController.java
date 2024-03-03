package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sj.noveling.entity.Member;
import sj.noveling.exception.NoPermissionException;
import sj.noveling.form.AddCommentForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final MemberService memberService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{chapter_id}")
    public String addCommentDo(
            Principal principal,
            @PathVariable("chapter_id") Long chapterId,
            @Validated AddCommentForm addCommentForm,
            BindingResult bindingResult
    ) {
        Member member = memberService.getMember(principal.getName());
        addCommentForm.setChapterId(chapterId);

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            bindingResult.reject("fail", "회차 등록에 실패하였습니다.");
            return "addChapter";
        }

        try {
            commentService.addComment(addCommentForm, member.getId());
        } catch (Exception e) {
            bindingResult.reject("fail", "회차 등록에 실패하였습니다.");
            return "addChapter";
        }

        return "redirect:/chapter/" + addCommentForm.getChapterId();
    }

    @GetMapping("/remove/{comment_id}")
    public String removeCommentDo(
//            Principal principal,
            @PathVariable(name = "comment_id") Long commentId
    ) {
//        Member member = memberService.getMember(principal.getName());
//
//        if (member == null || !member.getName().equals(principal.getName())) {
//            throw new NoPermissionException("수정권한이 없습니다.");
//        }

        Long chapterId = commentService.getChapterId(commentId);
        commentService.removeComment(commentId);
        return "redirect:/chapter/" + chapterId;
    }
}
