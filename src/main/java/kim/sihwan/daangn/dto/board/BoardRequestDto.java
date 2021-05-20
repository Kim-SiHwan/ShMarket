package kim.sihwan.daangn.dto.board;

import kim.sihwan.daangn.domain.board.Board;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class BoardRequestDto {
    private String nickname;
    private String title;
    private String content;
    private String area;
    private String category;
    private String hasImages;
    private List<MultipartFile> files;

    public Board toEntity(BoardRequestDto requestDto){
        return Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .area(requestDto.getArea())
                .category(requestDto.getCategory())
                .build();
    }

}
