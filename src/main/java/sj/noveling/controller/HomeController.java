package sj.noveling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sj.noveling.dto.CommentDto;
import sj.noveling.dto.NovelSimpleDto;
import sj.noveling.entity.Member;
import sj.noveling.exception.NoPermissionException;
import sj.noveling.form.JoinForm;
import sj.noveling.service.ChapterService;
import sj.noveling.service.CommentService;
import sj.noveling.service.MemberService;
import sj.noveling.service.NovelService;
import sj.noveling.type.Genre;

import java.security.Principal;
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

    @PostMapping("/join")
    public String joinDo(
            @Validated JoinForm joinForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            bindingResult.reject("fail", "회원가입 실패");
            return "join";
        }

        try {
            Member member = memberService.join(joinForm);
        } catch (Exception e) {
            bindingResult.reject("fail", e.getMessage());
            return "join";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String myPage(
            Principal principal, // 스프링 시큐리티가 제공하는 Principal 객체 : 현재 로그인한 사용자에 대한 정보 조회
            Model model
    ) {
        Member member = memberService.getMember(principal.getName());

        if (member == null || !member.getName().equals(principal.getName())) {
            throw new NoPermissionException("수정권한이 없습니다.");
        }

        List<NovelSimpleDto> novels = memberService.getMyNovels(member.getId());
        List<CommentDto> comments = memberService.getMyComments(member.getId());

        model.addAttribute("member", member);
        model.addAttribute("novels", novels);
        model.addAttribute("comments", comments);
        return "myPage";
    }

    @GetMapping("/removeMember/{member_id}")
    public String removeMember(
            @PathVariable("member_id") Long memberId
    ) {
        if (memberId == null) {
            return "redirect:/";
        }
        memberService.removeMember(memberId);
        return "redirect:/logout";
    }
}
