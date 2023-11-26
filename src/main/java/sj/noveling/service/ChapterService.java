package sj.noveling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sj.noveling.dto.*;
import sj.noveling.entity.Chapter;
import sj.noveling.entity.Novel;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.form.AddChapterForm;
import sj.noveling.form.SetChapterForm;
import sj.noveling.form.SetNovelForm;
import sj.noveling.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChapterService {

    private final MemberRepository memberRepository;
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;
    private final QueryDslRepository queryDslRepository;

    // 회차 리스트 조회
    public List<ChapterSimpleDto> getChapters(Long novelId) {
        return queryDslRepository.getChapters(novelId)
                .stream().map(Chapter::toChapterSimpleDto).collect(Collectors.toList());
    }

    // 회차 조회
    public ChapterDetailDto getChapter(Long chapterId) {
        Optional<Chapter> chapter = chapterRepository.findById(chapterId);

        if (chapter.isPresent()) {
            return chapter.get().toChapterDetailDto();
        } else {
            throw new DataNotFoundException("회차가 존재하지 않습니다.");
        }
    }

    public SetChapterForm getChapterForm(Long chapterId) {
        Optional<Chapter> originResult = chapterRepository.findById(chapterId);
        if (originResult.isPresent()) {
            return new SetChapterForm(
                    originResult.get().getId(),
                    originResult.get().getNovel().getId(),
                    originResult.get().getTitle(),
                    originResult.get().getContent()
            );
        } else {
            throw new DataNotFoundException("회차가 존재하지 않습니다.");
        }
    }

    // 회차 등록
    public Chapter addChapter(AddChapterForm form) {

        Optional<Novel> novel = novelRepository.findById(form.getNovelId());

        if (novel.isPresent()) {
            novel.get().updateDate();

            return chapterRepository.save(new Chapter(
                form.getTitle(),
                form.getContent(),
                novel.get()
            ));
        } else {
            throw new DataNotFoundException("소설이 존재하지 않습니다.");
        }
    }

    // 회차 수정
    public Chapter setChapter(SetChapterForm form) {

        Optional<Chapter> chapter = chapterRepository.findById(form.getId());

        if (chapter.isPresent()) {
            chapter.get().setTitle(form.getTitle());
            chapter.get().setContent(form.getContent());
            chapter.get().updateDate();
            return chapterRepository.save(chapter.get());
        } else {
            throw new DataNotFoundException("작품이 존재하지 않습니다.");
        }
    }

    // 회차 삭제
    public Boolean removeChapter(Long chapterId) {

        Optional<Chapter> chapter = chapterRepository.findById(chapterId);

        if (chapter.isPresent()) {
            chapterRepository.delete(chapter.get());
            return true;
        } else {
            return false;
        }
    }

    public Long getNovelId(Long chapterId) {

        Optional<Chapter> chapter = chapterRepository.findById(chapterId);

        if (chapter.isPresent()) {
            return chapter.get().getNovel().getId();
        } else {
            throw new DataNotFoundException("회차가 속한 작품이 존재하지 않습니다.");
        }
    }
}
