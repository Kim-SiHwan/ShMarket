package kim.sihwan.daangn.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequestDto {
    private Long id;
    private String nickname;
    private String newName;
    private String password;
    private String area;
}
