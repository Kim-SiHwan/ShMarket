package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Notice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Notice> findAllByMemberNickname(String nickname);

    int countAllByMemberNicknameAndChecked(String nickname, boolean read);
}
