package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {

}
