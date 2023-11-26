package sj.noveling.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import sj.noveling.type.Genre;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class NovelSimpleDto {

    private Long id;
    private String title;
    private String cover;
    private Genre genre;
    private String name;
}