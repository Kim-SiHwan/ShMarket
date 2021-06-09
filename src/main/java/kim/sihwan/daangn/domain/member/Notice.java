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
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;
    private Long target;
    private String type;
    private String message;
    private boolean isRead;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Notice(Long target, String type, String message) {
        this.target = target;
        this.type = type;
        this.message = message;
        this.isRead = false;
        this.createDate = LocalDateTime.now();
    }

    public void addMember(Member member){
        this.member = member;
    }
}
