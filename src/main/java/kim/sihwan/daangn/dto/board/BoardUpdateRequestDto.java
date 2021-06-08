package kim.sihwan.daangn.dto.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
    private String hasImages;
    private List<Long> ids;
    private List<MultipartFile> files;
}
