package kim.sihwan.daangn.dto.product;

import kim.sihwan.daangn.domain.product.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductRequestDto {

    @NotBlank(message = "상품 등록 지역은 필수 항목입니다.")
    private String area;

    @NotBlank(message = "상품 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "상품 내용은 필수 항목입니다.")
    private String content;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "상품 카테고리는 필수 항목입니다.")
    private String category;

    private String price;
    private String hasImages;
    private List<MultipartFile> files;
    private Set<String> tags;

    public Product toEntity(ProductRequestDto requestDto){
        return Product.builder()
                .area(requestDto.getArea())
                .price(requestDto.getPrice())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .nickname(requestDto.getNickname())
                .category(requestDto.getCategory())
                .build();
    }


}
