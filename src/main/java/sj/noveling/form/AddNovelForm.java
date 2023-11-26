package sj.noveling.form;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import sj.noveling.type.Genre;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class AddNovelForm {

    @NotEmpty(message = "필수 입력 항목입니다.")
    @Size(max=64, message = "64 글자 이내로 입력해주세요.")
    private String title;

    @Size(max=256, message = "256 글자 이내로 입력해주세요.")
    private String description;

    private String cover;

    @NotNull(message = "장르를 선택해주세요.")
    private Genre genre;
}
