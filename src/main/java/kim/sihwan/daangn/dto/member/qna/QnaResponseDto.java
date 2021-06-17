package kim.sihwan.daangn.dto.member.qna;

import kim.sihwan.daangn.domain.member.Qna;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class QnaResponseDto {
    private final Long id;
    private final String nickname;
    private final String title;
    private final String content;
    private final boolean answered;
    private final String answer;
    private final String createDate;

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
