package kim.sihwan.daangn.repository.product;

import kim.sihwan.daangn.domain.product.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTagRepository extends JpaRepository<ProductTag,Long> {
    List<ProductTag> findAllByTagId(Long tagId);

}
