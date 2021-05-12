package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findMemberByUsername(String username);
    Member findMemberByNickname(String nickname);

}
