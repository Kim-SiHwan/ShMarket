package kim.sihwan.daangn.repository.board;

import kim.sihwan.daangn.domain.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
