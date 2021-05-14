package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.board.BoardListResponseDto;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardListResponseDto>> findAllBoardsByCategory(@RequestParam List<String> categories){
        return new ResponseEntity<>(boardService.findAllBoardByCategory(categories), HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long boardId){
        return new ResponseEntity<>(boardService.findById(boardId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addBoard(@ModelAttribute BoardRequestDto boardRequestDto){
        boardService.addBoard(boardRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
        return new ResponseEntity(HttpStatus.OK);
    }
}