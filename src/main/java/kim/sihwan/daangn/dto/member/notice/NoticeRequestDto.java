package kim.sihwan.daangn.dto.member.notice;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NoticeRequestDto {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;
}
