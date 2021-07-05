package kim.sihwan.daangn.domain.chat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_log_id")
    private Long id;
    private String message;
    private String sender;
    private String receiver;
    private LocalDateTime createDate;
    private boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public void changeCheckedState() {
        this.checked = true;
    }

    @Builder
    public ChatLog(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.createDate = LocalDateTime.now();
        this.checked = false;
    }

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        this.chatRoom.getChatLogs().add(this);
    }
}
