package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.board.comment.CommentRequestDto;
import kim.sihwan.daangn.dto.board.comment.CommentResponseDto;
import kim.sihwan.daangn.dto.board.comment.CommentUpdateRequestDto;
import kim.sihwan.daangn.service.board.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponseDto>> findAllCommentsByBoardId(@PathVariable Long boardId){
        return new ResponseEntity<>(commentService.findAllCommentsByBoardId(boardId), HttpStatus.OK);
    }

    @PostMapping
    public void addComment(@Valid @RequestBody CommentRequestDto requestDto){
        commentService.addComment(requestDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }

    @PutMapping
    public void updateComment(@Valid @RequestBody CommentUpdateRequestDto updateRequestDto){
        commentService.updateComment(updateRequestDto);
    }
}
