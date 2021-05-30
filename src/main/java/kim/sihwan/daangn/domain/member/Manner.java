package kim.sihwan.daangn.domain.member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Manner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manner_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Manner(String content) {
        this.content = content;
    }

    public void addMember(Member member) {
        this.member = member;
        this.member.getManners().add(this);
    }


}
