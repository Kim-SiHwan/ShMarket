package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Review> findAllByMemberNickname(String nickname);
}
