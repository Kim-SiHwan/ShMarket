package kim.sihwan.daangn.repository.area;

import kim.sihwan.daangn.domain.area.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area,Long> {
    List<Area> findAllByCityLike(String city);
    Area findByAddress(String address);
}
