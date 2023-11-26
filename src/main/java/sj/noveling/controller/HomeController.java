package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sj.noveling.dto.CommentDto;
import sj.noveling.dto.NovelSimpleDto;
import sj.noveling.entity.Member;
import sj.noveling.form.JoinForm;
import sj.noveling.form.LoginForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;
import sj.noveling.type.Genre;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberService memberService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final CommentService commentService;

    @ModelAttribute("genres")
    public Genre[] genres() {
        return Genre.values();
    }

    @GetMapping("/")
    public String home(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value = "query", required = false) String query,
            Model model
    ) {
        Page<NovelSimpleDto> novels = novelService.getNovels(page, query, null);
        model.addAttribute("novels", novels);
        return "index";
    }

    @GetMapping("/genre/{genre}")
    public String genre(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value = "query", required = false) String query,
            @PathVariable("genre") String genre,
            Model model
    ) {
        Genre type;
        switch (genre) {
            case "무협":
                type = Genre.CHINESE;
                break;
            case "현대":
                type = Genre.MODERN;
                break;
            case "역사":
                type = Genre.HISTORY;
                break;
            case "판타지":
            default:
                type = Genre.FANTASY;
                break;
        }

        Page<NovelSimpleDto> novels = novelService.getNovels(page, query, type);
        model.addAttribute("novels", novels);
        model.addAttribute("genre", type);
        return "index";
    }

    @GetMapping("/join")
    public String join(
            @ModelAttribute JoinForm joinForm,
            BindingResult bindingResult
    ) {
        return "join";
    }

    @GetMapping("/login")
    public String login(
            @ModelAttribute LoginForm loginForm,
            BindingResult bindingResult
    ) {
        return "login";
    }

    @GetMapping("/myPage")
    public String myPage(
            Model model
    ) {
        Member member = memberService.getMember(1L);
        List<NovelSimpleDto> novels = memberService.getMyNovels(1L);
        List<CommentDto> comments = memberService.getMyComments(1L);

        model.addAttribute("member", member);
        model.addAttribute("novels", novels);
        model.addAttribute("comments", comments);
        return "myPage";
    }
}
