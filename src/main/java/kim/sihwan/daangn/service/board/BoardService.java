package kim.sihwan.daangn.service.board;


import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.repository.board.BoardRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardAlbumService boardAlbumService;

    @Transactional
    public Long addBoard(BoardRequestDto boardRequestDto){
        Board board = boardAlbumService.addProductAlbums(boardRequestDto);
        Member member = memberRepository.findMemberByNickname(boardRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);;
        board.addMember(member);
        boardRepository.save(board);
        return board.getId();
    }

}
