package kim.sihwan.daangn.repository.product;

import kim.sihwan.daangn.domain.product.ProductInterested;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestedRepository extends JpaRepository<ProductInterested,Long> {
    ProductInterested findByMemberUsernameAndProductId(String username, Long productId);
    ProductInterested findByMemberIdAndProductId(Long memberId, Long productId);
    List<ProductInterested> findAllByMemberNickname(String nickname);
}
