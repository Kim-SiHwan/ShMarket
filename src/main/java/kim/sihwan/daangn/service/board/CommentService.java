package kim.sihwan.daangn.service.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.board.Comment;
import kim.sihwan.daangn.dto.board.comment.CommentRequestDto;
import kim.sihwan.daangn.dto.board.comment.CommentResponseDto;
import kim.sihwan.daangn.dto.board.comment.CommentUpdateRequestDto;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void addComment(CommentRequestDto commentRequestDto){
        Comment comment = commentRequestDto.toEntity(commentRequestDto);
        Board board = boardRepository.findById(commentRequestDto.getBoardId()).orElseThrow(AlreadyGoneException::new);
        comment.addBoard(board);
    }

    public List<CommentResponseDto> findAllCommentsByBoardId(Long boardId){
        return commentRepository.findAllByBoardId(boardId)
                .stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void updateComment(CommentUpdateRequestDto updateRequestDto){
        Comment comment = commentRepository.findById(updateRequestDto.getId()).orElseThrow(AlreadyGoneException::new);
        comment.changeComment(updateRequestDto.getContent());

    }
}
