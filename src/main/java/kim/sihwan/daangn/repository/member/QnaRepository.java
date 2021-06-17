package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna,Long> {

    List<Qna> findAllByMemberNickname(String nickname);
}
