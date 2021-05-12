package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {


}
