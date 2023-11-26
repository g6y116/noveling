package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sj.noveling.dto.NovelDetailDto;
import sj.noveling.form.AddNovelForm;
import sj.noveling.form.SetNovelForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;
import sj.noveling.type.Genre;

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

        return "novel";
    }

    @GetMapping("/add/{member_id}")
    public String addNovel(
            @ModelAttribute AddNovelForm addNovelForm,
            @PathVariable("member_id") Long memberId,
            Model model
    ) {
        return "addNovel";
    }

    @GetMapping("/set/{novel_id}")
    public String setNovel(
            @ModelAttribute SetNovelForm setNovelForm,
            @PathVariable("novel_id") Long novelId,
            Model model
    ) {
        return "setNovel";
    }
}
