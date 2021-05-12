package kim.sihwan.daangn.repository.tag;

import kim.sihwan.daangn.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
}
