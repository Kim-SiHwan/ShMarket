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
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;
    private String title;
    private String content;
    private String answer;
    private boolean visited;
    private boolean answered;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void visitedQna(){
        this.visited=true;
    }

    public void answeredQna(){
        this.answered=true;
    }

    @Builder
    public Qna(String title, String content) {
        this.title = title;
        this.content = content;
        this.visited = false;
        this.answer = "";
        this.answered = false;
        this.createDate = LocalDateTime.now();
    }

    public void addMember(Member member) {
        this.member = member;
    }
}
