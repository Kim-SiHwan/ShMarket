package kim.sihwan.daangn.dto.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.dto.board.boardAlbum.BoardAlbumResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BoardResponseDto {
    private final Long id;
    private final String area;
    private final String title;
    private final String content;
    private final String nickname;
    private final String updateDate;
    private final String category;
    private final boolean hasImages;
    private final List<BoardAlbumResponseDto> boardAlbums;

    public static BoardResponseDto toDto(Board board) {
        return BoardResponseDto
                .builder()
                .id(board.getId())
                .area(board.getArea())
                .nickname(board.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .updateDate(board.getUpdateDate().toString())
                .category(board.getCategory())
                .boardAlbums(
                        board.getBoardAlbums()
                                .stream()
                                .map(BoardAlbumResponseDto::new)
                                .sorted(Comparator.comparing(BoardAlbumResponseDto::getId, Comparator.reverseOrder()))
                                .collect(Collectors.toList())
                )
                .hasImages(board.getBoardAlbums().isEmpty())
                .build();
    }
}
