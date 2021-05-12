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
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    public void addMember(Member member){
        this.member = member;
    }
}
