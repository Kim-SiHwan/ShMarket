package kim.sihwan.daangn.dto.board;

import kim.sihwan.daangn.domain.board.Board;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class BoardRequestDto {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @Size(max = 13, message = "동네생활 제목은 13자 이하로 작성해주세요.")
    @NotBlank(message = "동네생활 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "동네생활 내용은 필수 항목입니다.")
    private String content;

    @NotBlank(message = "지역은 필수 항목입니다.")
    private String area;

    @NotBlank(message = "카테고리는 필수 항목입니다.")
    private String category;

    private String hasImages;
    private List<MultipartFile> files;

    public Board toEntity(BoardRequestDto requestDto) {
        return Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .area(requestDto.getArea())
                .category(requestDto.getCategory())
                .build();
    }

}
