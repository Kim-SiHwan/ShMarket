package kim.sihwan.daangn.repository.product;

import kim.sihwan.daangn.domain.product.ProductLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike,Long> {

    @EntityGraph(attributePaths = {"product", "member"})
    Optional<ProductLike> findByMemberUsernameAndProductId(String username, Long productId);

    @EntityGraph(attributePaths = {"product", "member"})
    List<ProductLike> findAllByMemberNickname(String nickname);
}
