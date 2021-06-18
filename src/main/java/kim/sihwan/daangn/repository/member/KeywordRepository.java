package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword,Long> {

    Optional<Keyword> findByKeyword(String keyword);
}
