package kim.sihwan.daangn.service.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.BoardUpdateRequestDto;
import kim.sihwan.daangn.dto.board.QBoardDto;
import kim.sihwan.daangn.dto.common.PagingDto;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.repository.board.BoardQueryRepository;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardQueryRepository queryRepository;

    private final BoardAlbumService boardAlbumService;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void addBoard(BoardRequestDto boardRequestDto) {
        Board board = boardRequestDto.toEntity(boardRequestDto);

        if (boardRequestDto.getHasImages().equals("yes")) {
            board = boardAlbumService.addBoardAlbum(boardRequestDto);
        }

        Member member = memberRepository.findMemberByNickname(boardRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);
        board.addMember(member);
        boardRepository.save(board);
    }

    public BoardResponseDto findById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(AlreadyGoneException::new);
        return BoardResponseDto.toDto(board);
    }

    public Result findAllBoardByNickname(int page, String nickname) {
        List<QBoardDto> list = queryRepository.findBoards(page,20,nickname);
        int totalPage = boardRepository.boardCountByNickname(nickname)/ 20;
        return new Result(list,totalPage);
    }

    public Result paging(int page, List<String> categories, String nickname) {
        Member member = findMemberByUsername();
        ListOperations<String, String> vo = redisTemplate.opsForList();

        PagingDto pagingDto = new PagingDto(member,vo,categories);
        List<QBoardDto> list = queryRepository.findBoards(page,20,nickname).stream()
                .filter(board -> pagingDto.getAreaList().contains(board.getArea()) && pagingDto.getCategories().contains(board.getCategory()) && !pagingDto.getBlockList().contains(board.getNickname()))
                .collect(Collectors.toList());
        int totalPage = boardRepository.boardCount() / 20;

        return new Result(list, totalPage);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(AlreadyGoneException::new);
        String nickname = findMemberByUsername().getNickname();
        if (!board.getMember().getNickname().equals(nickname)) {
            throw new NotMineException();
        }
        boardRepository.delete(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(BoardUpdateRequestDto boardUpdateRequestDto) {
        Board board = boardRepository.findById(boardUpdateRequestDto.getId()).orElseThrow(AlreadyGoneException::new);
        String nickname = findMemberByUsername().getNickname();
        if (!board.getMember().getNickname().equals(nickname)) {
            throw new NotMineException();
        }
        if (!boardUpdateRequestDto.getIds().isEmpty()) {
            boardAlbumService.deleteImages(board, boardUpdateRequestDto.getIds());
        }
        if (boardUpdateRequestDto.getHasImages().equals("yes")) {
            boardAlbumService.appendImages(board, boardUpdateRequestDto.getFiles());
        }
        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
        return BoardResponseDto.toDto(board);
    }

    @Transactional
    public Member findMemberByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findMemberByUsername(username);
    }
}
