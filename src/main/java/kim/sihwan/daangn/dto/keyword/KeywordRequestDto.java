package kim.sihwan.daangn.dto.keyword;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class KeywordRequestDto {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "키워드는 필수 항목입니다.")
    private String keyword;



}
