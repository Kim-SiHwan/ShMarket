package kim.sihwan.daangn.domain.member;


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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    private String content;
    private String buyer;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Review(Long id, String content, String buyer) {
        this.id = id;
        this.content = content;
        this.buyer = buyer;
        this.createDate = LocalDateTime.now();
    }

    public void addMember(Member member) {
        this.member = member;
        this.member.getReviews().add(this);
    }

}
