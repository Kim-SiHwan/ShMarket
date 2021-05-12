package kim.sihwan.daangn.domain.area;


import kim.sihwan.daangn.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selected_area_id")
    private Long id;
    private String place;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Area area;

    public void addMember(Member member){
        this.member = member;
    }

    public void addArea(Area area) {
        this.area = area;

    }
}
