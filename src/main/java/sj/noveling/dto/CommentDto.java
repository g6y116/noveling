package sj.noveling.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private String name; // 작성자
}