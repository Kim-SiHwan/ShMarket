package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.MemberKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword,Long> {
    List<MemberKeyword> findAllByMemberUsername(String username);
}