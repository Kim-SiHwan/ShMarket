package kim.sihwan.daangn.dto.keyword;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordRequestDto {
    private Long memberId;
    private String nickname;
    private String keyword;


}
