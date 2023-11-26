package sj.noveling.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sj.noveling.entity.Chapter;
import sj.noveling.entity.Novel;
import sj.noveling.type.Genre;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static sj.noveling.entity.QMember.member;
import static sj.noveling.entity.QNovel.novel;
import static sj.noveling.entity.QChapter.chapter;

@Repository
// https://batory.tistory.com/496
public class QueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public QueryDslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 전체 작품 페이징
    public Page<Novel> pageNovels(String query, Genre genre, Pageable pageable) {
        QueryResults<Novel> results = queryFactory
                .selectFrom(novel).join(novel.member, member)
                .where(queryEq(query), genreEq(genre))
                .orderBy(novel.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();

        List<Novel> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression queryEq(String query) {
        return query != null ? (novel.title.contains(query)).or(novel.member.name.contains(query)) : null;
    }

    private BooleanExpression genreEq(Genre genre) {
        return genre != null ? novel.genre.eq(genre) : null;
    }

    // 최근 작품 리스트
    public List<Novel> newestNovels() {
        QueryResults<Novel> results = queryFactory
                .selectFrom(novel)
                .where(novel.modifyDate.isNotNull())
                .orderBy(novel.modifyDate.desc())
                .limit(3)
                .fetchResults();

        List<Novel> contents = results.getResults();
        return contents;
    }

    // 인기 작품 리스트
    public List<Novel> bestNovels() {
        QueryResults<Novel> results = queryFactory
                .selectFrom(novel)
                .where(novel.viewCount.ne(0))
                .orderBy(novel.viewCount.desc())
                .limit(3)
                .fetchResults();

        List<Novel> contents = results.getResults();
        return contents;
    }

    // 회차 리스트
    public List<Chapter> getChapters(Long novelId) {
        QueryResults<Chapter> results = queryFactory
                .selectFrom(chapter).where(chapter.novel.id.eq(novelId))
                .orderBy(chapter.id.desc())
                .fetchResults();

        List<Chapter> contents = results.getResults();
        return contents;
    }
}
