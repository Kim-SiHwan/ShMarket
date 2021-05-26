package kim.sihwan.daangn.dto.keyword;

import kim.sihwan.daangn.domain.member.MemberKeyword;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordListResponseDto {
    private Long id;
    private String keyword;

    public static KeywordListResponseDto toDto(MemberKeyword memberKeyword){
        return KeywordListResponseDto.builder()
                .id(memberKeyword.getId())
                .keyword(memberKeyword.getKeyword().getKeyword())
                .build();
    }


}
