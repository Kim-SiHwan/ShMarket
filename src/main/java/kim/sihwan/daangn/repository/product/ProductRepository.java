package kim.sihwan.daangn.repository.product;

import kim.sihwan.daangn.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
