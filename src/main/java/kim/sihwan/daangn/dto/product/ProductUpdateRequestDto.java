package kim.sihwan.daangn.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
    private String hasImages;
    private List<Long> ids;
    private List<MultipartFile> files;
}
