package kim.sihwan.daangn.dto.chat;

import kim.sihwan.daangn.domain.chat.ChatLog;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatLogResponseDto {
    private Long roomId;
    private String message;
    private String sender;
    private String receiver;
    private String createDate;

    public ChatLogResponseDto(ChatLog chatLog) {
        this.roomId = chatLog.getChatRoom().getId();
        this.message = chatLog.getMessage();
        this.sender = chatLog.getSender();
        this.receiver = chatLog.getReceiver();
        this.createDate = chatLog.getCreateDate().toString();
    }

    public ChatLogResponseDto(ChatRequestDto requestDto) {
        this.roomId = requestDto.getRoomId();
        this.sender = requestDto.getSender();
        this.receiver = requestDto.getReceiver();
        this.message = requestDto.getMessage();
        this.createDate = LocalDateTime.now().toString();
    }
}
