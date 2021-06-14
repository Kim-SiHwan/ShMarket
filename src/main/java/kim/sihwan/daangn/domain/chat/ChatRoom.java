package kim.sihwan.daangn.domain.chat;

import kim.sihwan.daangn.domain.product.Product;
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
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String sender;
    private String receiver;
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void changeDate() {
        this.updateDate = LocalDateTime.now();
    }

    @Builder
    public ChatRoom(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.updateDate = LocalDateTime.now();
    }

    public void addProduct(Product product) {
        this.product = product;
    }

}
