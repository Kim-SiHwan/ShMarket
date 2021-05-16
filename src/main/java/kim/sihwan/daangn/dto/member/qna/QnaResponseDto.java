package kim.sihwan.daangn.dto.member.qna;

import kim.sihwan.daangn.domain.member.Qna;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class QnaResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private boolean answered;
    private String answer;
    private String createDate;

    public static QnaResponseDto toDto(Qna qna){
        return QnaResponseDto.builder()
                .id(qna.getId())
                .nickname(qna.getMember().getNickname())
                .title(qna.getTitle())
                .content(qna.getContent())
                .answered(qna.isAnswered())
                .answer(qna.getAnswer())
                .createDate(qna.getCreateDate().toString())
                .build();
    }

}
