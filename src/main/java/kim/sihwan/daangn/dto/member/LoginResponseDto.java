package kim.sihwan.daangn.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Long id;
    private String token;
    private String username;
    private String nickname;
    private String area;

    public LoginResponseDto(Long id, String token, String username, String nickname, String area) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.nickname = nickname;
        this.area = area;
    }
}
