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

    @Query("SELECT count(p.id) FROM Product p")
    int productCount();

    @Query("SELECT count(p.nickname) FROM Product p WHERE p.nickname = :nickname")
    int productCountByNickname(@Param("nickname") String nickname);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"member"})
    List<Product> findAllByMemberNickname(String nickname);

}
