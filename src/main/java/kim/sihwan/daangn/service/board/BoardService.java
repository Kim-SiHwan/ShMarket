package kim.sihwan.daangn.service.board;


import kim.sihwan.daangn.domain.area.SelectedArea;
import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.board.BoardListResponseDto;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.BoardUpdateRequestDto;
import kim.sihwan.daangn.repository.area.SelectedAreaRepository;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardAlbumService boardAlbumService;
    private final SelectedAreaRepository selectedAreaRepository;
    private final RedisTemplate<String,String> redisTemplate;

    @Transactional
    public Long addBoard(BoardRequestDto boardRequestDto){
        Board board = boardAlbumService.addProductAlbums(boardRequestDto);
        Member member = memberRepository.findMemberByNickname(boardRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);;
        board.addMember(member);
        boardRepository.save(board);
        return board.getId();
    }

    public BoardResponseDto findById(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
        addRead(boardId);
        return BoardResponseDto.toDto(board);
    }

    public List<BoardListResponseDto> findAllBoardByCategory(List<String> categories) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findMemberByUsername(username);
        SelectedArea selectedArea = selectedAreaRepository.findByMemberId(member.getId());
        ListOperations<String, String> vo = redisTemplate.opsForList();

        List<String> al = vo.range(selectedArea.getArea().getAddress() + "::List", 0L, -1L);

        List<BoardListResponseDto> result = boardRepository.findAll()
                .stream()
                .filter(board -> al.contains(board.getArea()) && categories.contains(board.getCategory()))
                .map(BoardListResponseDto::toDto)
                .sorted(Comparator.comparing(BoardListResponseDto::getId, Comparator.reverseOrder())).collect(Collectors.toList());

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        return result;

    }
    @Transactional
    public void deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
        boardRepository.delete(board);
    }

    @Transactional
    public Board updateBoard(BoardUpdateRequestDto boardUpdateRequestDto){
        Board board = boardRepository.findById(boardUpdateRequestDto.getId()).orElseThrow(NoSuchElementException::new);
        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
        return board;
    }

    @Transactional
    public void addRead(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
        board.addRead();
    }
}
