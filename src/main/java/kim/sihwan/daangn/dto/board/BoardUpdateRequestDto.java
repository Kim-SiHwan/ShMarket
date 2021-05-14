package kim.sihwan.daangn.dto.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
}
