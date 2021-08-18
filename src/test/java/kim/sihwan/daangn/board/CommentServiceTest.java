package kim.sihwan.daangn.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.board.Comment;
import kim.sihwan.daangn.dto.board.comment.CommentRequestDto;
import kim.sihwan.daangn.dto.board.comment.CommentResponseDto;
import kim.sihwan.daangn.dto.board.comment.CommentUpdateRequestDto;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.board.CommentRepository;
import kim.sihwan.daangn.service.board.CommentService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    CommentRepository commentRepository;


    @Test
    @DisplayName("CommentService.addComment 댓글 작성 테스트")
    public void 댓글_추가테스트() throws Exception {

        //given
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setBoardId(1L);
        commentRequestDto.setNickname("guest");
        commentRequestDto.setContent("content");

        Board board = Board.builder()
                .nickname("user")
                .title("test Title")
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.ofNullable(board));

        //when
        commentService.addComment(commentRequestDto);

        //then
        assertEquals(1, board.getComments().size());
    }

    @Test
    @DisplayName("CommentService.findAllCommentsByBoardId 댓글 리스트 조회 태스트")
    public void 댓글_조회테스트() throws Exception {

        //given
        List<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Comment comment = Comment.builder()
                    .nickname("guest")
                    .content("content" + i)
                    .build();
            commentList.add(comment);
        }

        given(commentRepository.findAllByBoardId(anyLong())).willReturn(commentList);

        //when
        List<CommentResponseDto> list = commentService.findAllCommentsByBoardId(anyLong());

        //then
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("CommentService.updateComment 댓글 수정 테스트")
    public void 댓글_수정테스트() throws Exception {

        //given
        Comment comment = Comment.builder()
                .nickname("guest")
                .content("content")
                .build();

        CommentUpdateRequestDto updateRequestDto = new CommentUpdateRequestDto();
        updateRequestDto.setId(1L);
        updateRequestDto.setContent("수정 content");

        given(commentRepository.findById(anyLong())).willReturn(Optional.ofNullable(comment));

        //when
        commentService.updateComment(updateRequestDto);

        //then
        assertEquals("수정 content", comment.getContent());
    }
}
