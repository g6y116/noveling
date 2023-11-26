package sj.noveling.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import sj.noveling.dto.NovelDetailDto;
import sj.noveling.dto.NovelSimpleDto;
import sj.noveling.type.Genre;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long id;

    @Column(name = "novel_title", nullable = false, length = 64)
    private String title;

    @Column(name = "novel_description", length = 256)
    private String description;

    @Column(name = "novel_cover")
    private String cover;

    @Enumerated(EnumType.STRING)
    @Column(name = "novel_genre")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "novel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Column(name = "novel_view_count", columnDefinition = "INT DEFAULT 0")
    private int viewCount;

    public Novel(String title, String description, Genre genre, Member member) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.member = member;
        createDate = LocalDateTime.now();
        modifyDate = LocalDateTime.now();
        viewCount = 0;
    }

    public Novel(String title, String description, String cover, Genre genre, Member member) {
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.genre = genre;
        this.member = member;
        createDate = LocalDateTime.now();
        modifyDate = LocalDateTime.now();
        viewCount = 0;
    }

    public NovelSimpleDto toNovelSimpleDto() {
        return new NovelSimpleDto(
                getId(),
                getTitle(),
                getCover(),
                getGenre(),
                getMember().getName()
        );
    }

    public NovelDetailDto toNovelDetailDto() {
        List<Chapter> reverseChapters = getChapters();
        Collections.reverse(reverseChapters);
        return new NovelDetailDto(
                getId(),
                getTitle(),
                getDescription(),
                getCover(),
                getGenre(),
                getMember().getName(),
                reverseChapters.stream().map(Chapter::toChapterSimpleDto).collect(Collectors.toList())
        );
    }

    public void updateDate() {
        setModifyDate(LocalDateTime.now());
    }
}