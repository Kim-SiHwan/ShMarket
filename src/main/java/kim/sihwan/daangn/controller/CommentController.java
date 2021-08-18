package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "5. Comment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;


    @ApiOperation(value = "댓글 전체 조회", notes = "게시글의 PK로 해당 게시글의 전체 댓글 조회")
    @ApiImplicitParam(name = "boardId", dataType = "Long", value = "조회 대상 PK", required = true)
    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponseDto>> findAllCommentsByBoardId(@PathVariable Long boardId) {
        return new ResponseEntity<>(commentService.findAllCommentsByBoardId(boardId), HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 추가", notes = "댓글 추가")
    @PostMapping
    public void addComment(@Valid @RequestBody CommentRequestDto requestDto) {
        commentService.addComment(requestDto);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 PK로 댓글 삭제")
    @ApiImplicitParam(name = "commentId", dataType = "Long", value = "삭제 대상 PK", required = true)
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글 수정")
    @PutMapping
    public void updateComment(@Valid @RequestBody CommentUpdateRequestDto updateRequestDto) {
        commentService.updateComment(updateRequestDto);
    }
}
