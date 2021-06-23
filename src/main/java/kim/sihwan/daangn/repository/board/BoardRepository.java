package kim.sihwan.daangn.repository.board;

import kim.sihwan.daangn.domain.board.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query("SELECT b FROM Board b order by b.id desc ")
    @EntityGraph(attributePaths = {"member"})
    Slice<Board> findBoards(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Board> findById(Long id);

    @EntityGraph(attributePaths = {"member"})
    List<Board> findAllByMemberNickname(String nickname);

}
