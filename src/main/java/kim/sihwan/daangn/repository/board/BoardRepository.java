package kim.sihwan.daangn.repository.board;

import io.lettuce.core.dynamic.annotation.Param;
import kim.sihwan.daangn.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {


    @Query("SELECT count(b.id) from Board b")
    int boardCount();

    @Query("SELECT count(b.nickname) FROM Board b WHERE b.nickname =:nickname")
    int boardCountByNickname(@Param("nickname") String nickname);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Board> findById(Long id);

    @EntityGraph(attributePaths = {"member"})
    List<Board> findAllByMemberNickname(String nickname);

    @Query("SELECT b FROM Board b WHERE b.createDate < :time")
    @EntityGraph(attributePaths = {"member"})
    Page<Board> findBoards(Pageable pageable, @Param("time") LocalDateTime time);


}
