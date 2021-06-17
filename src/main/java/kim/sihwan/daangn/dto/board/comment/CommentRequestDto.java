package kim.sihwan.daangn.dto.board.comment;

import kim.sihwan.daangn.domain.board.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentRequestDto {

    private Long boardId;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "댓글 내용은 필수 항목입니다.")
    private String content;

    public Comment toEntity(CommentRequestDto requestDto){
        return Comment.builder()
                .content(requestDto.getContent())
                .nickname(requestDto.getNickname())
                .build();
    }
}
