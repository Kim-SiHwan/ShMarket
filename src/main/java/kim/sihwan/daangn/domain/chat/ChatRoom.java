package kim.sihwan.daangn.domain.chat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String sender;
    private String receiver;

    @Builder
    public ChatRoom(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
