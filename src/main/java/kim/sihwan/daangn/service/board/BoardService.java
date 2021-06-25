package kim.sihwan.daangn.service.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.member.Block;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.board.BoardListResponseDto;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.BoardUpdateRequestDto;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.member.BlockRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
     private final BlockRepository blockRepository;
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
        addRead(boardId);
        return BoardResponseDto.toDto(board);
    }

    public List<BoardListResponseDto> findAllBoardByNickname(String nickname) {
        return boardRepository.findAllByMemberNickname(nickname).stream()
                .map(BoardListResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public Result paging(int offset, List<String> categories) {
        Member member = findMemberByUsername();
        ListOperations<String, String> vo = redisTemplate.opsForList();

        List<String> getCategories = categories.stream()
                .map(m -> URLDecoder.decode(m, StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        List<String> al = vo.range(member.getArea() + "::List", 0L, -1L);

        List<String> blockList = blockRepository.findAllByMemberNickname(member.getNickname()).stream()
                .map(Block::getToMember)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(offset, 20);

        Slice<Board> page = boardRepository.findBoards(pageable, LocalDateTime.now());

        List<BoardListResponseDto> result = page.stream().parallel()
                .filter(board -> al.contains(board.getArea()) && getCategories.contains(board.getCategory()) && !blockList.contains(board.getMember().getNickname()))
                .map(BoardListResponseDto::toDto)
                .collect(Collectors.toList());

        return new Result(result, page.hasNext());
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
    public void addRead(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(AlreadyGoneException::new);
        board.addRead();
    }

    @Transactional
    public Member findMemberByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findMemberByUsername(username);
    }
}
