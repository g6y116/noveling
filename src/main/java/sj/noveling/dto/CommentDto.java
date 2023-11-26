package sj.noveling.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private String name; // 작성자
    private LocalDateTime date;
}