package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block,Long> {

    Optional<Block> findByMemberNicknameAndToMember(String fromNickname, String toNickname);

    List<Block> findAllByMemberNickname(String nickname);
}
