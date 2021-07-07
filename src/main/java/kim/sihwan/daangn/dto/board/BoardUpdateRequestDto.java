package kim.sihwan.daangn.dto.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class BoardUpdateRequestDto {

    private Long id;

    @NotBlank(message = "변경할 게시글 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "변경할 게시글 내용은 필수 항목입니다.")
    private String content;

    private String hasImages;
    private List<Long> ids;
    private List<MultipartFile> files;
}
