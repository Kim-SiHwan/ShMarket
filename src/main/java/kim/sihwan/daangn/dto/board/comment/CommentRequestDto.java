package kim.sihwan.daangn.dto.board.comment;

import kim.sihwan.daangn.domain.board.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long boardId;
    private String nickname;
    private String content;

    public Comment toEntity(CommentRequestDto requestDto){
        return Comment.builder()
                .content(requestDto.getContent())
                .nickname(requestDto.getNickname())
                .build();
    }
}
