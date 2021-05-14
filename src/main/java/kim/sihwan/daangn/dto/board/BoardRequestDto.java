package kim.sihwan.daangn.dto.board;

import kim.sihwan.daangn.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardRequestDto {
    private String nickname;
    private String title;
    private String content;
    private String area;
    private String category;
    private List<MultipartFile> files;

    public Board toEntity(BoardRequestDto requestDto){
        return Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())

                .createDate(LocalDateTime.now())
                .build();
    }

}
