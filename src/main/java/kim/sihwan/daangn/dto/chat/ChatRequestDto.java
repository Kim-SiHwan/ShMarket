package kim.sihwan.daangn.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDto {
    private Long roomId;
    private Long productId;
    private String sender;
    private String receiver;
    private String message;
    private String fcmToken;


}
