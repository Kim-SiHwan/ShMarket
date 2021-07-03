package kim.sihwan.daangn.dto.product;

import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.dto.product.productAlbum.ProductAlbumResponseDto;
import kim.sihwan.daangn.dto.tag.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductResponseDto {
    private final Long id;
    private final String area;
    private final String title;
    private final String content;
    private final String nickname;
    private final String category;
    private final String createDate;
    private final String status;
    private final boolean like;
    private final boolean hasImages;
    private final List<ProductAlbumResponseDto> productAlbums;
    private final List<TagResponseDto> tags;

    public static ProductResponseDto toDto(Product product, boolean like) {
        return ProductResponseDto
                .builder()
                .id(product.getId())
                .area(product.getArea())
                .nickname(product.getNickname())
                .status(product.getStatus().getDescription())
                .like(like)
                .hasImages(product.getProductAlbums().isEmpty())
                .title(product.getTitle())
                .content(product.getContent())
                .category(product.getCategory())
                .createDate(product.getCreateDate().toString())
                .productAlbums(
                        product.getProductAlbums().stream()
                                .map(ProductAlbumResponseDto::new)
                                .sorted(Comparator.comparing(ProductAlbumResponseDto::getId, Comparator.reverseOrder()))
                                .collect(Collectors.toList())
                )
                .tags(product.getProductTags().stream()
                        .map(tag -> new TagResponseDto(tag.getTag()))
                        .sorted(Comparator.comparing(TagResponseDto::getId, Comparator.reverseOrder()))
                        .collect(Collectors.toList()))
                .build();
    }
}
