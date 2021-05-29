package kim.sihwan.daangn.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDto {
    private Long roomId;
    private String lastMessage;
    private String thumbnail;

}
