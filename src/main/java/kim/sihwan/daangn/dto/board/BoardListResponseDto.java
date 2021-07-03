package kim.sihwan.daangn.dto.board;

import kim.sihwan.daangn.domain.board.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardListResponseDto {
    private final Long id;
    private final String area;
    private final String title;
    private final String content;
    private final String nickname;
    private final String updateDate;
    private final String thumbnail;
    private final String category;
    private final int boardAlbumCount;
    private final int commentCount;

    public static BoardListResponseDto toDto(Board board) {
        return BoardListResponseDto
                .builder()
                .id(board.getId())
                .area(board.getArea())
                .nickname(board.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .updateDate(board.getUpdateDate().toString())
                .category(board.getCategory())
                .thumbnail(board.getThumbnail())
                .boardAlbumCount(board.getBoardAlbums().size())
                .commentCount(board.getComments().size())
                .build();
    }
}
