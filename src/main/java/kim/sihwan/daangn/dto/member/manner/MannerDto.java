package kim.sihwan.daangn.dto.member.manner;

import kim.sihwan.daangn.domain.member.Manner;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class MannerDto {

    private Long id;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "평가 타입은 필수 항목입니다.")
    private String type;

    @NotBlank(message = "평가 내용은 필수 항목입니다.")
    private String content;

    public Manner toEntity(MannerDto mannerDto) {
        return Manner.builder()
                .type(mannerDto.getType())
                .content(mannerDto.getContent())
                .build();
    }

    public static MannerDto toDto(Manner manner) {
        return MannerDto.builder()
                .id(manner.getId())
                .nickname(manner.getMember().getNickname())
                .type(manner.getType())
                .content(manner.getContent())
                .build();
    }
}
