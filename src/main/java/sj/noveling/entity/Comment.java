package sj.noveling.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import sj.noveling.dto.CommentDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content", nullable = false, length = 128)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    public Comment(String content, Chapter chapter, Member member) {
        this.content = content;
        this.chapter = chapter;
        this.member = member;
        createDate = LocalDateTime.now();
        modifyDate = LocalDateTime.now();
    }

    public CommentDto toCommentDto() {
        return new CommentDto(
                getId(),
                getContent(),
                getMember().getName(),
                getModifyDate()
        );
    }
}