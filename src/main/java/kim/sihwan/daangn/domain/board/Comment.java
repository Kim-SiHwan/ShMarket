package kim.sihwan.daangn.domain.board;


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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Comment (String nickname, String content){
        this.nickname = nickname;
        this.content = content;
        this.createDate = LocalDateTime.now();
    }

    public void addBoard(Board board){
        this.board = board;
        this.board.getComments().add(this);
    }


}
