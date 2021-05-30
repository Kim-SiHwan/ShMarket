package kim.sihwan.daangn.dto.member.manner;

import kim.sihwan.daangn.domain.member.Manner;
import kim.sihwan.daangn.domain.member.Review;
import kim.sihwan.daangn.dto.member.review.ReviewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MannerDto {
    private String nickname;
    private String type;
    private String content;

    public Manner toEntity(MannerDto mannerDto){
        return Manner.builder()
                .type(mannerDto.getType())
                .content(mannerDto.getContent())
                .build();
    }

    public static MannerDto toDto(Manner manner){
        return MannerDto.builder()
                .nickname(manner.getMember().getNickname())
                .type(manner.getType())
                .content(manner.getContent())
                .build();
    }
}
