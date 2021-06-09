package kim.sihwan.daangn.domain.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_album_id")
    private Long id;
    private String url;
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public BoardAlbum(String url, String filename) {
        this.url = url;
        this.filename = filename;
    }

    public void addBoard(Board board) {
        this.board = board;
        this.board.getBoardAlbums().add(this);
    }
}
