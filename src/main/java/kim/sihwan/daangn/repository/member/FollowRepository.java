package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFromMemberNickname(String nickname);

    Follow findByFromMemberNicknameAndToMemberNickname(String from, String to);
}
