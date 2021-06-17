package kim.sihwan.daangn.repository.board;

import kim.sihwan.daangn.domain.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    @Override
    @EntityGraph(attributePaths = {"member", "boardAlbums"})
    List<Board> findAll();

    List<Board> findALlByMemberNickname(String nickname);
}
