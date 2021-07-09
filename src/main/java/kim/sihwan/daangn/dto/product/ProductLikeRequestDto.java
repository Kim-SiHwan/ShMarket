package kim.sihwan.daangn.dto.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductLikeRequestDto {

    @NotBlank(message = "등록할 상품 아이디는 필수 항목입니다.")
    private Long productId;
}
