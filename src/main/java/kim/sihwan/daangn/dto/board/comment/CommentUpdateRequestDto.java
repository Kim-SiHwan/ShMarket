package kim.sihwan.daangn.dto.board.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentUpdateRequestDto {

    private Long id;

    @NotBlank(message = "변경할 댓글 내용은 필수 항목입니다.")
    private String content;
}
