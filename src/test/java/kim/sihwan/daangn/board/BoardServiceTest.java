package kim.sihwan.daangn.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.QBoardDto;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.repository.board.BoardQueryRepository;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.service.board.BoardService;
import kim.sihwan.daangn.service.member.MemberService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BoardQueryRepository boardQueryRepository;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    ListOperations<String, String> listOperations;

    @Mock
    MemberService memberService;

    private BoardRequestDto boardRequestDto;

    private Member member;

    private Board board;

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));

        boardRequestDto = BoardRequestDto.builder()
                .title("test Title")
                .content("test Content")
                .hasImages("no")
                .nickname("user")
                .area("?????????")
                .category("??????????????????")
                .build();

        member = Member.builder()
                .username("user")
                .nickname("user")
                .area("?????????")
                .build();

        board = Board.builder()
                .title("test Title")
                .content("test Content")
                .nickname("user")
                .area("?????????")
                .category("??????????????????")
                .build();

        board.addMember(member);
    }


    @Test
    @DisplayName("BoardService.addBoard ????????? ?????? ?????????")
    public void ?????????_???????????????() throws Exception {

        //given
        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        boardService.addBoard(boardRequestDto);

        //then
        verify(memberRepository, times(1)).findMemberByNickname(boardRequestDto.getNickname());
        verify(boardRepository, times(1)).save(any(Board.class));

    }

    @Test
    @DisplayName("BoardService.findById ????????? ?????? ?????? ?????????")
    public void ?????????_??????_???????????????() throws Exception {

        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.ofNullable(board));

        //when
        BoardResponseDto boardResponseDto = boardService.findById(anyLong());

        //then
        verify(boardRepository, times(1)).findById(anyLong());
        assertEquals(board.getCategory(), boardResponseDto.getCategory());
        assertEquals(board.getTitle(), boardResponseDto.getTitle());

    }

    @Test(expected = AlreadyGoneException.class)
    @DisplayName("BoardService.findById ????????? ?????? ?????? ?????????")
    public void ?????????_??????_?????????????????????() throws Exception {

        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        boardService.findById(anyLong());

        //then

    }

    @Test
    @DisplayName("BoardService.paging ?????? ????????? ?????? ?????????")
    public void ?????????_??????_???????????????() throws Exception {

        //given
        QBoardDto qBoardDto = new QBoardDto(1L, "?????????", "?????????", "??????", "??????", LocalDateTime.now(), "?????????", "????????????", 0, 0);

        List<QBoardDto> qBoardDtoList = new ArrayList<>();
        qBoardDtoList.add(qBoardDto);

        ArrayList<String> areaList = new ArrayList<>();
        areaList.add("?????????");

        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(redisTemplate.opsForList()).willReturn(listOperations);
        given(boardQueryRepository.findBoards(anyInt(), anyInt(), anyString(), anyList(), anyList(), anyList())).willReturn(qBoardDtoList);
        given(memberService.setNearArea(anyString())).willReturn(areaList);

        //when
        Result result = boardService.paging(anyInt(), new ArrayList<>(), member.getNickname());

        //then
        verify(memberRepository, times(1)).findMemberByUsername(anyString());
        verify(redisTemplate, times(1)).opsForList();
        verify(boardQueryRepository, times(1)).findBoards(anyInt(), anyInt(), anyString(), anyList(), anyList(), anyList());

    }

    @Test
    @DisplayName("BoardService.deleteBoard ????????? ?????? ?????????")
    public void ?????????_???????????????() throws Exception {

        //given
        given(boardRepository.findById(anyLong())).willReturn(Optional.ofNullable(board));
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        boardService.deleteBoard(anyLong());

        //then
        verify(boardRepository, times(1)).delete(board);
    }

    @Test(expected = NotMineException.class)
    @DisplayName("BoardService.deleteBoard ???????????? ????????? ?????? ?????? ??? ?????? ?????? ?????????")
    public void ?????????_?????????????????????() throws Exception {

        //given
        Member tempMember = Member.builder()
                .nickname("guest")
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.ofNullable(board));
        given(memberRepository.findMemberByUsername(anyString())).willReturn(tempMember);

        //when
        boardService.deleteBoard(anyLong());

        //then

    }


}
