package kim.sihwan.daangn.repository.product;

import kim.sihwan.daangn.domain.product.ProductInterested;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestedRepository extends JpaRepository<ProductInterested,Long> {
}
