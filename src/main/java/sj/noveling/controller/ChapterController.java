package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sj.noveling.dto.ChapterDetailDto;
import sj.noveling.entity.Member;
import sj.noveling.exception.NoPermissionException;
import sj.noveling.form.AddChapterForm;
import sj.noveling.form.AddNovelForm;
import sj.noveling.form.SetChapterForm;
import sj.noveling.form.SetNovelForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;
import sj.noveling.type.Genre;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chapter")
public class ChapterController {

    private final MemberService memberService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final CommentService commentService;

    @ModelAttribute("genres")
    public Genre[] genres() {
        return Genre.values();
    }

    @GetMapping("/{id}")
    public String chapter(
            @PathVariable("id") Long chapterId,
            Model model
    ) {
        ChapterDetailDto chapterDetailDto = chapterService.getChapter(chapterId);
        model.addAttribute("chapter", chapterDetailDto);

        return "chapter";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{novel_id}")
    public String addChapter(
            Principal principal,
            @PathVariable("novel_id") Long novelId,
            @ModelAttribute AddChapterForm addChapterForm,
            Model model
    ) {
        Member member = memberService.getMember(principal.getName());
        addChapterForm.setNovelId(novelId);

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        return "addChapter";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{novel_id}")
    public String addChapterDo(
            Principal principal,
            @PathVariable("novel_id") Long novelId,
            @Validated AddChapterForm addChapterForm,
            BindingResult bindingResult
    ) {
        Member member = memberService.getMember(principal.getName());
        addChapterForm.setNovelId(novelId);

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            bindingResult.reject("fail", "회차 등록에 실패하였습니다.");
            return "addChapter";
        }

        try {
            chapterService.addChapter(addChapterForm);
        } catch (Exception e) {
            bindingResult.reject("fail", "회차 등록에 실패하였습니다.");
            return "addChapter";
        }

        return "redirect:/novel/" + addChapterForm.getNovelId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/set/{chapter_id}")
    public String setChapter(
            Principal principal,
            @ModelAttribute SetChapterForm setChapterForm,
            @PathVariable("chapter_id") Long chapterId,
            Model model
    ) {
        Member member = memberService.getMember(principal.getName());
        setChapterForm.setId(chapterId);

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        SetChapterForm originChapterForm = chapterService.getChapterForm(chapterId);
        setChapterForm.setId(originChapterForm.getId());
        setChapterForm.setNovelId(originChapterForm.getNovelId());
        setChapterForm.setTitle(originChapterForm.getTitle());
        setChapterForm.setContent(originChapterForm.getContent());

        return "setChapter";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/set/{chapter_id}")
    public String setChapterDo(
            Principal principal,
            @PathVariable("chapter_id") Long chapterId,
            @Validated SetChapterForm setChapterForm,
            BindingResult bindingResult
    ) {
        Member member = memberService.getMember(principal.getName());
        setChapterForm.setId(chapterId);

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            bindingResult.reject("fail", "회차 수정에 실패하였습니다.");
            return "setChapter";
        }

        try {
            chapterService.setChapter(setChapterForm);
        } catch (Exception e) {
            bindingResult.reject("fail", "회차 수정에 실패하였습니다.");
            return "setChapter";
        }

        return "redirect:/novel/" + setChapterForm.getNovelId();
    }

    @GetMapping("/remove/{chapter_id}")
    public String removeChapterDo(
            Principal principal,
            @PathVariable(name = "chapter_id") Long chapterId
    ) {
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        Long novelId = chapterService.getNovelId(chapterId);
        chapterService.removeChapter(chapterId);
        return "redirect:/novel/" + novelId;
    }
}
