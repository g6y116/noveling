package sj.noveling.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import sj.noveling.type.Genre;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class NovelDetailDto {

    private Long id;
    private String title;
    private String description;
    private String cover;
    private Genre genre;
    private String name;

    private List<ChapterSimpleDto> chapters;
}