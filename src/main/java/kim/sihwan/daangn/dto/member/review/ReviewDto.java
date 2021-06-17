package kim.sihwan.daangn.dto.member.review;

import kim.sihwan.daangn.domain.member.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class ReviewDto {

    @NotBlank(message = "판매자의 닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "구매자의 닉네임은 필수 항목입니다.")
    private String buyer;

    @NotBlank(message = "거래 후기 내용은 필수 항목입니다.")
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
