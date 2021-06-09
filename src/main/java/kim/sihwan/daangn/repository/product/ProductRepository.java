package kim.sihwan.daangn.repository.product;

import kim.sihwan.daangn.domain.product.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {"member", "productTags", "productTags.tag", "productAlbums", "productInteresteds"})
    List<Product> findAll();

    @Override
    @EntityGraph(attributePaths = {"member", "productTags", "productTags.tag", "productInteresteds"})
    Optional<Product> findById(Long id);
}
