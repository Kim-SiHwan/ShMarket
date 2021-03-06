package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.MemberKeyword;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword, Long> {

    @EntityGraph(attributePaths = {"member", "keyword"})
    List<MemberKeyword> findAllByMemberNickname(String nickname);

    @EntityGraph(attributePaths = {"member", "keyword"})
    List<MemberKeyword> findAllByKeywordKeyword(String keyword);

    @EntityGraph(attributePaths = {"member", "keyword"})
    Optional<MemberKeyword> findByMemberNicknameAndKeywordKeyword(String nickname, String keyword);

    int countAllByMemberNicknameAndKeywordKeyword(String nickname, String keyword);
}

