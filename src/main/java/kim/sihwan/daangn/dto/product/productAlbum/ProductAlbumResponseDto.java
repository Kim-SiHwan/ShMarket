package kim.sihwan.daangn.dto.product.productAlbum;

import kim.sihwan.daangn.domain.product.ProductAlbum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAlbumResponseDto {
    private final Long id;
    private final String url;
    private final String filename;

    public ProductAlbumResponseDto(ProductAlbum productAlbum) {
        this.id = productAlbum.getId();
        this.url = productAlbum.getUrl();
        this.filename = productAlbum.getFilename();
    }
}
