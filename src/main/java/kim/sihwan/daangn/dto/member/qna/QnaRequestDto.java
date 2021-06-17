package kim.sihwan.daangn.dto.member.qna;

import kim.sihwan.daangn.domain.member.Qna;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QnaRequestDto {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "문의 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "문의 내용은 필수 항목입니다.")
    private String content;

    public Qna toEntity(QnaRequestDto requestDto){
        return Qna.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

    }
}
