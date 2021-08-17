package kim.sihwan.daangn.service.board;


import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.board.BoardAlbum;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.repository.board.BoardAlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAlbumService {

    private final BoardAlbumRepository boardAlbumRepository;

    private static final String fileUrl = "C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\images\\";
    private static final String saveUrl = "http://localhost:8080/api/board/download?fileName=";
    private String addUrl = "";

    @Transactional
    public Board addBoardAlbum(BoardRequestDto boardRequestDto) {
        Board board = boardRequestDto.toEntity(boardRequestDto);
        addImage(board, boardRequestDto.getFiles());
        return board;
    }

    @Transactional
    public void appendImages(Board board, List<MultipartFile> files) {
        addImage(board, files);
    }

    @Transactional
    public void addImage(Board board, List<MultipartFile> files) {
        Long tempThumbnailId = 0L;
        try {
            for (MultipartFile file : files) {
                String newFilename = createNewFilename(file.getOriginalFilename());
                addUrl = saveUrl + newFilename;
                File dest = new File(fileUrl + newFilename);
                file.transferTo(dest);

                BoardAlbum boardAlbum = BoardAlbum.builder()
                        .filename(file.getOriginalFilename())
                        .url(addUrl)
                        .build();
                boardAlbum.addBoard(board);
                tempThumbnailId = boardAlbum.getId();
            }
            if (board.getThumbnail().equals(saveUrl + "default.png")) {
                board.addThumbnail(tempThumbnailId, addUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteImages(Board board, List<Long> ids) {
        boardAlbumRepository.deleteAllByBoardAlbumIds(ids);
        if (ids.contains(board.getThumbnailId()) || board.getBoardAlbums().size() == 0) {
            board.removeThumbnail();
            return;
        }
        List<BoardAlbum> list = new ArrayList<>(board.getBoardAlbums());
        board.addThumbnail(list.get(0).getId(), list.get(0).getUrl());

    }

    public String createNewFilename(String filename) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + filename;
    }


}
