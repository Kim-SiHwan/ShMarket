package kim.sihwan.daangn.dto.board.boardAlbum;

import kim.sihwan.daangn.domain.board.BoardAlbum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardAlbumResponseDto {
    private final Long id;
    private final String url;
    private final String filename;

    public BoardAlbumResponseDto(BoardAlbum boardAlbum) {
        this.id = boardAlbum.getId();
        this.url = boardAlbum.getUrl();
        this.filename = boardAlbum.getFilename();
    }
}
