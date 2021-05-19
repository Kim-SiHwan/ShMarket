package kim.sihwan.daangn.dto.product;

import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.dto.product.productAlbum.ProductAlbumResponseDto;
import kim.sihwan.daangn.dto.tag.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductResponseDto {
    private final Long id;
    private final String area;
    private final String title;
    private final String content;
    private final String username;
    private final String nickname;
    private final String category;
    private final String createDate;
    private boolean like;
    private final int readCount;
    private final List<ProductAlbumResponseDto> productAlbums;
    private final List<TagResponseDto> tags;

    public static ProductResponseDto toDto(Product product, boolean like){
        return ProductResponseDto
                .builder()
                .id(product.getId())
                .area(product.getArea())
                .username(product.getMember().getUsername())
                .nickname(product.getMember().getNickname())
                .like(like)
                .title(product.getTitle())
                .content(product.getContent())
                .category(product.getCategory())
                .createDate(product.getCreateDate().toString())
                .productAlbums(product.getProductAlbums()
                        .stream()
                        .map(ProductAlbumResponseDto::new)
                        .collect(Collectors.toList())
                )
                .tags(product.getProductTags()
                        .stream()
                        .map(tag-> new TagResponseDto(tag.getTag()))
                        .collect(Collectors.toList()))
                .build();
    }
}
