package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Manner;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MannerRepository extends JpaRepository<Manner,Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Manner> findAllByMemberNickname(String nickname);
}
