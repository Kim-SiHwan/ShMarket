package kim.sihwan.daangn.dto.member.qna;

import kim.sihwan.daangn.domain.member.Qna;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaRequestDto {

    private Long memberId;
    private String title;
    private String content;

    public Qna toEntity(QnaRequestDto requestDto){
        return Qna.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

    }
}
