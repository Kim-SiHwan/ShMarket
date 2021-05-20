package kim.sihwan.daangn.service.board;


import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.board.BoardAlbum;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAlbumService {

    @Transactional
    public Board addBoardAlbum(BoardRequestDto boardRequestDto){
        Board board = boardRequestDto.toEntity(boardRequestDto);
        String fileUrl = "C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\static\\images\\";
        String saveUrl = "http://localhost:8080/api/board/download?fileName=";
        String addUrl = "";
        try{
            for(MultipartFile file : boardRequestDto.getFiles()){
                String newFilename = createNewFilename(file.getOriginalFilename());
                addUrl = saveUrl + newFilename;
                File dest = new File(fileUrl + newFilename);
                file.transferTo(dest);

                BoardAlbum boardAlbum = BoardAlbum.builder()
                        .filename(file.getOriginalFilename())
                        .url(addUrl)
                        .build();
                boardAlbum.addBoard(board);

            }
            board.addThumbnail(addUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        return board;
    }

    public String createNewFilename(String filename){
        UUID uuid = UUID.randomUUID();
        String newFilename= uuid.toString() +"_" + filename;
        return newFilename;
    }


}
