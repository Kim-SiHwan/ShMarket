package kim.sihwan.daangn.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class ProductUpdateRequestDto {

    private Long id;

    @NotBlank(message = "변경할 상품 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "변경할 상품 내용은 필수 항목입니다.")
    private String content;

    private String hasImages;
    private List<Long> ids;
    private List<MultipartFile> files;
}
