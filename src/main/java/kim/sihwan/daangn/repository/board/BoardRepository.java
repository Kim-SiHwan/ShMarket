package kim.sihwan.daangn.repository.board;

import kim.sihwan.daangn.domain.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Board> findById(Long id);

    @EntityGraph(attributePaths = {"member"})
    List<Board> findAllByMemberNickname(String nickname);

}
