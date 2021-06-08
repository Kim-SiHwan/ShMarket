package kim.sihwan.daangn.domain.board;


import kim.sihwan.daangn.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


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
    private String area;
    private String category;
    private Long thumbnailId;
    private String thumbnail;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int read;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<BoardAlbum> boardAlbums = new HashSet<>();

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

    public void addThumbnail(Long thumbnailId, String thumbnail) {
        this.thumbnailId = thumbnailId;
        this.thumbnail = thumbnail;
    }

    public void removeThumbnail() {
        this.thumbnail = "http://localhost:8080/api/board/download?fileName=default.png";
        this.thumbnailId = 0L;
    }

    @Builder
    public Board(String title, String content, int read, String area, String category) {
        this.title = title;
        this.content = content;
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.area = area;
        this.category = category;
        this.thumbnailId = 0L;
        this.thumbnail = "http://localhost:8080/api/board/download?fileName=default.png";
        this.read = read;
    }

    public void addMember(Member member) {
        this.member = member;
    }


}
