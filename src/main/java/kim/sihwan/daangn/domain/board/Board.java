package kim.sihwan.daangn.domain.board;


import kim.sihwan.daangn.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int read;


    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void addRead() {
        this.read++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }

    @Builder
    public Board(String title, String content, LocalDateTime createDate, int read) {
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.read = read;
    }

    public void addMember(Member member) {
        this.member = member;
    }


}
