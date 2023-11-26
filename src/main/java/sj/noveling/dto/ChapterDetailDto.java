package sj.noveling.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ChapterDetailDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private int commentCnt;
    private List<CommentDto> comments;
}