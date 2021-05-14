package kim.sihwan.daangn.dto.board.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {
    private Long id;
    private String content;
}
