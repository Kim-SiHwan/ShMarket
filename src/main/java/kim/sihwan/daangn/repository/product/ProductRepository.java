package kim.sihwan.daangn.repository.product;

import io.lettuce.core.dynamic.annotation.Param;
import kim.sihwan.daangn.domain.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"member"})
    List<Product> findAllByMemberNickname(String nickname);

    @Query("SELECT p FROM Product p WHERE p.createDate < :time")
    @EntityGraph(attributePaths = {"member"})
    Slice<Product> findProducts(Pageable pageable, @Param("time") LocalDateTime time);

}
