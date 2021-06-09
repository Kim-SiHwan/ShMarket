package kim.sihwan.daangn.dto.product;

import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.dto.tag.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductListResponseDto {
    private final Long id;
    private final String area;
    private final String title;
    private final String content;
    private final String nickname;
    private final String createDate;
    private final String thumbnail;
    private final String category;
    private final int readCount;
    private final int productAlbumCount;
    private final boolean like;
    private final List<TagResponseDto> tags;

    public static ProductListResponseDto toDto(Product product, boolean like){
        return ProductListResponseDto
                .builder()
                .id(product.getId())
                .area(product.getArea())
                .nickname(product.getMember().getNickname())
                .title(product.getTitle())
                .content(product.getContent())
                .createDate(product.getCreateDate().toString())
                .category(product.getCategory())
                .thumbnail(product.getThumbnail())
                .productAlbumCount(product.getProductAlbums().size())
                .like(like)
                .tags(
                        product.getProductTags()
                        .stream()
                        .map(tag-> new TagResponseDto(tag.getTag()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
