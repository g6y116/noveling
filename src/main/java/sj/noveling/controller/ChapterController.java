package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sj.noveling.dto.ChapterDetailDto;
import sj.noveling.form.AddChapterForm;
import sj.noveling.form.SetChapterForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;
import sj.noveling.type.Genre;

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

    @GetMapping("/chapter/{id}")
    public String chapter(
            @PathVariable("id") Long chapterId,
            Model model
    ) {
        ChapterDetailDto chapterDetailDto = chapterService.getChapter(chapterId);
        model.addAttribute("chapter", chapterDetailDto);

        return "chapter";
    }

    @GetMapping("/add/{novel_id}")
    public String addChapter(
            @ModelAttribute AddChapterForm addChapterForm,
            @PathVariable("novel_id") Long chapterId,
            Model model
    ) {
        return "addChapter";
    }

    @GetMapping("/set/{chapter_id}")
    public String setChapter(
            @ModelAttribute SetChapterForm setChapterForm,
            @PathVariable("chapter_id") Long chapterId,
            Model model
    ) {
        return "setChapter";
    }
}
