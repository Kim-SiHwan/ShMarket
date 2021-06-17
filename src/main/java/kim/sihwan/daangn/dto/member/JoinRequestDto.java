package kim.sihwan.daangn.dto.member;

import kim.sihwan.daangn.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
public class JoinRequestDto {

    @Size(min = 6, max = 12)
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = "^(?=\\S+$)(?=.*?[a-z])(?=.*?[0-9]).{6,13}$", message ="아이디는 공백 없이 특수문자를 제외한 영문자와 숫자 조합으로  6~12글자로 작성해주세요." )
    private String username;

    @Size(min = 6, max = 12)
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=\\S+$)(?=.*?[a-z])(?=.*?[0-9]).{6,13}$" , message ="비밀번호는 공백 없이 영문자와 숫자 조합으로 6~12글자로 작성해주세요..")
    private String password;

    @Size(min = 6, max = 12)
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Pattern(regexp = "^(?=\\S+$)(?=.*?[a-z])(?=.*?[0-9]).{6,13}$", message ="아이디는 공백 없이 특수문자를 제외한 영문자와 숫자 조합으로  6~12글자로 작성해주세요." )
    private String nickname;

    @NotBlank(message = "거주지는 필수 항목입니다.")
    private String area;

    public Member toEntity(JoinRequestDto requestDto, PasswordEncoder passwordEncoder){
        return Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .area(requestDto.getArea())
                .role("ROLE_USER")
                .build();
    }
}
