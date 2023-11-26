package sj.noveling.form;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class JoinForm {

    @NotEmpty(message = "필수 입력 항목입니다.")
    @Size(min=2, max=16, message = "2 ~ 16 글자로 입력해주세요.")
    private String name;

    @NotEmpty(message = "필수 입력 항목입니다.")
    @Size(min=4, max=16, message = "4 ~ 16 글자로 입력해주세요.")
    private String loginId;

    @NotEmpty(message = "필수 입력 항목입니다.")
    @Size(min=8, max=24, message = "8 ~ 24 글자로 입력해주세요.")
    private String loginPw;
}
