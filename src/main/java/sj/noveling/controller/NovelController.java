package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sj.noveling.dto.NovelDetailDto;
import sj.noveling.dto.NovelSimpleDto;
import sj.noveling.entity.Member;
import sj.noveling.entity.Novel;
import sj.noveling.exception.NoPermissionException;
import sj.noveling.form.AddNovelForm;
import sj.noveling.form.SetNovelForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;
import sj.noveling.type.Genre;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/novel")
public class NovelController {

    private final MemberService memberService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final CommentService commentService;

    @ModelAttribute("genres")
    public Genre[] genres() {
        return Genre.values();
    }

    @GetMapping("/{id}")
    public String novel(
            @PathVariable("id") Long novelId,
            Model model
    ) {
        NovelDetailDto novelDetailDto = novelService.getNovel(novelId);
        model.addAttribute("novel", novelDetailDto);

        List<NovelSimpleDto> newNovels = novelService.getNewestNovels();
        model.addAttribute("newNovels", newNovels);

        List<NovelSimpleDto> bestNovels = novelService.getBestNovels();
        model.addAttribute("bestNovels", bestNovels);
        return "novel";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String addNovel(
            Principal principal,
            @ModelAttribute AddNovelForm addNovelForm,
            Model model
    ) {
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        return "addNovel";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addNovelDo(
            Principal principal,
            @Validated AddNovelForm addNovelForm,
            BindingResult bindingResult
    ) {
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            bindingResult.reject("fail", "작품 등록에 실패하였습니다.");
            return "addNovel";
        }

        try {
            Novel novel = novelService.addNovel(addNovelForm, member);
            return "redirect:/novel/" + novel.getId();
        } catch (Exception e) {
            bindingResult.reject("fail", "작품 등록에 실패하였습니다.");
            return "addNovel";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/set/{novel_id}")
    public String setNovel(
            Principal principal,
            @ModelAttribute SetNovelForm setNovelForm,
            @PathVariable("novel_id") Long novelId,
            Model model
    ) {
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        SetNovelForm originNovelForm = novelService.getNovelForm(novelId);
        setNovelForm.setId(originNovelForm.getId());
        setNovelForm.setTitle(originNovelForm.getTitle());
        setNovelForm.setDescription(originNovelForm.getDescription());
        setNovelForm.setCover(originNovelForm.getCover());
        setNovelForm.setGenre(originNovelForm.getGenre());

        return "setNovel";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/set/{novel_id}")
    public String setNovelDo(
            Principal principal,
            @PathVariable("novel_id") Long novelId,
            @Validated SetNovelForm setNovelForm,
            BindingResult bindingResult
    ) {
        setNovelForm.setId(novelId);
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            bindingResult.reject("fail", "작품 수정에 실패하였습니다.");
            return "setNovel";
        }

        try {
            Novel novel = novelService.setNovel(setNovelForm);
            return "redirect:/novel/" + novel.getId();
        } catch (Exception e) {
            bindingResult.reject("fail", "작품 수정에 실패하였습니다.");
            return "setNovel";
        }
    }

    @GetMapping("/remove/{novel_id}")
    public String removeNovelDo(
            Principal principal,
            @PathVariable(name = "novel_id") Long novelId
    ) {
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        novelService.removeNovel(novelId);
        return "redirect:/";
    }
}
