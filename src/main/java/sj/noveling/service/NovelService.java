package sj.noveling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sj.noveling.Const;
import sj.noveling.dto.NovelDetailDto;
import sj.noveling.dto.NovelSimpleDto;
import sj.noveling.entity.Member;
import sj.noveling.entity.Novel;
import sj.noveling.exception.DataNotFoundException;
import sj.noveling.form.AddNovelForm;
import sj.noveling.form.SetNovelForm;
import sj.noveling.repository.*;
import sj.noveling.type.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NovelService {

    private final MemberRepository memberRepository;
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;
    private final QueryDslRepository queryDslRepository;

    // 전체 작품 페이징
    public Page<NovelSimpleDto> getNovels(int page, String query, Genre genre) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Novel> originResults = queryDslRepository.pageNovels(query, genre, pageable);
        List<NovelSimpleDto> listResults
                = originResults.getContent().stream().map(Novel::toNovelSimpleDto).collect(Collectors.toList());

        return new PageImpl<>(listResults, pageable, originResults.getTotalElements());
    }

    // 작품 조회
    public NovelDetailDto getNovel(Long novelId) {
        Optional<Novel> originResult = novelRepository.findById(novelId);

        if (originResult.isPresent()) {
            return originResult.get().toNovelDetailDto();
        } else {
            throw new DataNotFoundException("작품이 존재하지 않습니다.");
        }
    }

    // 인기 작품 리스트 조회
    public List<NovelSimpleDto> getBestNovels() {
        List<Novel> originResults = queryDslRepository.bestNovels();

        return originResults.stream().map(Novel::toNovelSimpleDto).collect(Collectors.toList());
    }

    // 내 작품 리스트 조회
    public List<NovelSimpleDto> getMyNovels(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            return novelRepository.findByMember(member.get()).stream().map(Novel::toNovelSimpleDto).collect(Collectors.toList());
        } else {
            throw new DataNotFoundException("작가가 존재하지 않습니다.");
        }
    }

    // 작품 등록
    public Novel addNovel(AddNovelForm form, Member member) {

        Novel novel = novelRepository.save(new Novel(
            form.getTitle(),
            form.getDescription(),
            form.getCover(),
            form.getGenre(),
            member
        ));

        return novel;
    }

    // 작품 수정
    public Novel setNovel(SetNovelForm form) {

        Optional<Novel> novel = novelRepository.findById(form.getId());

        if (novel.isPresent()) {
            novel.get().setTitle(form.getTitle());
            novel.get().setDescription(form.getDescription());
            novel.get().setCover(form.getCover());
            novel.get().setGenre(form.getGenre());
            return novelRepository.save(novel.get());
        } else {
            throw new DataNotFoundException("작품이 존재하지 않습니다.");
        }
    }

    // 작품 삭제
    public Boolean removeNovel(Long novelId) {

        Optional<Novel> novel = novelRepository.findById(novelId);

        if (novel.isPresent()) {
            novelRepository.delete(novel.get());
            return true;
        } else {
            return false;
        }
    }
}
