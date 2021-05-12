package kim.sihwan.daangn.repository.area;

import kim.sihwan.daangn.domain.area.SelectedArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectedAreaRepository extends JpaRepository<SelectedArea,Long> {
    SelectedArea findByMemberId(Long memberId);
}
