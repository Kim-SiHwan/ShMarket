package kim.sihwan.daangn.service.board;

import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.member.Block;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.board.*;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.repository.board.BoardQueryRepository;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    private final MemberService memberService;
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
        List<QBoardDto> list = queryRepository.findBoards(page,20,nickname,null,null,null);
        int totalPage = boardRepository.boardCountByNickname(nickname)/ 20;
        return new Result(list,totalPage);
    }

  public Result paging(int page, List<String> categories, String nickname) {
      Member member = findMemberByUsername();
      String getMembersArea = member.getArea();

      ListOperations<String, String> vo = redisTemplate.opsForList();

      //카테고리 리스트
      List<String> categoryList = categories.stream()
              .map(m -> URLDecoder.decode(m, StandardCharsets.UTF_8))
              .collect(Collectors.toList());

      //주변 동네 리스트
      List<String> areaList = vo.range(getMembersArea + "::List", 0L, -1L);

      //없을 경우 DB에서 조회 후 Redis에 저장
      if (areaList.isEmpty()) {
          areaList = memberService.setNearArea(getMembersArea);
          vo.leftPushAll(getMembersArea + "::List", areaList);
      }

      //차단 사용자 리스트
      List<String> blockList = member.getBlocks().stream()
              .map(Block::getToMember)
              .collect(Collectors.toList());

      //필터링 후 데이터 조회
      List<QBoardDto> QBoardList = queryRepository.findBoards(page,20,nickname,blockList,areaList,categoryList);

      int totalPage = boardRepository.boardCount() / 20;

      return new Result(QBoardList, totalPage);
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
