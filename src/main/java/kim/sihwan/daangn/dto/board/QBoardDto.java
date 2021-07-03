package kim.sihwan.daangn.dto.board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QBoardDto {
    private Long id;
    private String area;
    private String title;
    private String content;
    private String nickname;
    private String updateDate;
    private String thumbnail;
    private String category;
    private int boardAlbumCount;
    private int commentCount;

    public QBoardDto(Long id, String area, String title, String content, String nickname, LocalDateTime updateDate, String thumbnail, String category, int boardAlbumCount, int commentCount) {
        this.id = id;
        this.area = area;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.updateDate = updateDate.toString();
        this.thumbnail = thumbnail;
        this.category = category;
        this.boardAlbumCount = boardAlbumCount;
        this.commentCount = commentCount;
    }
}
