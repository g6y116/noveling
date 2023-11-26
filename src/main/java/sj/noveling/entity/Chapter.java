package sj.noveling.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import sj.noveling.dto.ChapterDetailDto;
import sj.noveling.dto.ChapterSimpleDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chapter_id")
    private Long id;

    @Column(name = "chapter_title", nullable = false, length = 64)
    private String title;

    @Column(name = "chapter_content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private Novel novel;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    public Chapter(String title, String content, Novel novel) {
        this.title = title;
        this.content = content;
        this.novel = novel;
        this.createDate = LocalDateTime.now();
    }

    public ChapterSimpleDto toChapterSimpleDto() {
        return new ChapterSimpleDto(
                getId(),
                getTitle(),
                getComments().size()
        );
    }

    public ChapterDetailDto toChapterDetailDto() {

        LocalDateTime date;

        if(getModifyDate() != null)
            date = getModifyDate() ;
        else
            date = getCreateDate();

        return new ChapterDetailDto(
                getId(),
                getTitle(),
                getContent(),
                date,
                getComments().size(),
                getComments().stream().map(Comment::toCommentDto).collect(Collectors.toList())
        );
    }

    public void updateDate() {
        novel.setModifyDate(LocalDateTime.now());
        setModifyDate(LocalDateTime.now());
    }
}