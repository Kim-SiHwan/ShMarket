package kim.sihwan.daangn.domain.member;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
