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
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    private String toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Block(String toMember) {
        this.toMember = toMember;
    }

    public void addMember(Member member) {
        this.member = member;
        this.member.getBlocks().add(this);
    }

}
