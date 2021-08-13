package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.BoardUpdateRequestDto;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list/{page}")
    public ResponseEntity<Result> getBoardsByPaging(@RequestParam(value = "list") List<String> categories,
                                                    @RequestParam(required = false) String nickname,
                                                    @PathVariable(required = false) int page) {
        return new ResponseEntity<>(boardService.paging(page, categories, nickname), HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long boardId) {
        return new ResponseEntity<>(boardService.findById(boardId), HttpStatus.OK);
    }

    @GetMapping("/my/{page}")
    public ResponseEntity<Result> findAllBoardByNickname(@RequestParam String nickname,
                                                         @PathVariable int page) {
        return new ResponseEntity<>(boardService.findAllBoardByNickname(page, nickname), HttpStatus.OK);
    }

    @GetMapping(value = "/download", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        Resource resource = new FileSystemResource("C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\images\\" + fileName);
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @PostMapping
    public void addBoard(@Valid @ModelAttribute BoardRequestDto boardRequestDto) {
        boardService.addBoard(boardRequestDto);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }

    @PutMapping
    public ResponseEntity<BoardResponseDto> updateBoard(@Valid @ModelAttribute BoardUpdateRequestDto updateRequestDto) {
        return new ResponseEntity<>(boardService.updateBoard(updateRequestDto), HttpStatus.OK);
    }
}
