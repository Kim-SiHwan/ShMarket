package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.board.BoardListResponseDto;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.BoardUpdateRequestDto;
import kim.sihwan.daangn.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardListResponseDto>> findAllBoardsByCategory(@RequestParam(value = "list") List<String> categories) {
        return new ResponseEntity<>(boardService.findAllBoardByCategory(categories), HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long boardId) {
        return new ResponseEntity<>(boardService.findById(boardId), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BoardListResponseDto>> findAllBoardByNickname(@RequestParam String nickname){
        return new ResponseEntity<>(boardService.findAllBoardByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping(value = "/download", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        Resource resource = new FileSystemResource("C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\static\\images\\" + fileName);
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @PostMapping
    public void addBoard(@ModelAttribute BoardRequestDto boardRequestDto) {
        boardService.addBoard(boardRequestDto);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }

    @PutMapping
    public ResponseEntity<BoardResponseDto> updateBoard(@ModelAttribute BoardUpdateRequestDto updateRequestDto) {
        return new ResponseEntity<>(boardService.updateBoard(updateRequestDto), HttpStatus.OK);
    }
}
