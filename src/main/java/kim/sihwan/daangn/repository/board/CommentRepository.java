package kim.sihwan.daangn.repository.board;

import kim.sihwan.daangn.domain.board.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @EntityGraph(attributePaths = {"board"})
    List<Comment> findAllByBoardId(Long boardId);
}
