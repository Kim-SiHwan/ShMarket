package kim.sihwan.daangn.dto.member.review;

import kim.sihwan.daangn.domain.member.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewDto {
    private String nickname;
    private String buyer;
    private String content;

    public Review toEntity(ReviewDto reviewDto){
        return Review.builder()
                .buyer(reviewDto.getBuyer())
                .content(reviewDto.getContent())
                .build();
    }

    public static ReviewDto toDto(Review review){
        return ReviewDto.builder()
                .nickname(review.getMember().getNickname())
                .buyer(review.getBuyer())
                .content(review.getContent())
                .build();
    }

}
