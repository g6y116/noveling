package sj.noveling.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ChapterSimpleDto {

    private Long id;
    private String title;
    private int commentCnt;
}