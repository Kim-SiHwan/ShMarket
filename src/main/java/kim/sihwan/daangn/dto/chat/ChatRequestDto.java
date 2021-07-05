package kim.sihwan.daangn.dto.chat;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChatRequestDto {

    private Long roomId;
    private Long productId;

    @NotBlank(message = "채팅 발송인은 필수 항목입니다.")
    private String sender;

    @NotBlank(message = "채팅 수신인은 필수 항목입니다.")
    private String receiver;

    @NotBlank(message = "채팅 메시지는 필수 항목입니다.")
    private String message;

}
