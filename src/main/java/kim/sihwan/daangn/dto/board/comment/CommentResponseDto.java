package kim.sihwan.daangn.dto.board.comment;

import kim.sihwan.daangn.domain.board.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private final Long id;
    private final String nickname;
    private final String content;
    private final String updateDate;

    public static CommentResponseDto toDto(Comment comment){
        return CommentResponseDto
                .builder()
                .id(comment.getId())
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .updateDate(comment.getUpdateDate().toString())
                .build();
    }
}
