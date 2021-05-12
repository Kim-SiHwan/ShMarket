package kim.sihwan.daangn.repository.member;

import kim.sihwan.daangn.domain.member.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
