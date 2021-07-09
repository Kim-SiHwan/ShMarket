package kim.sihwan.daangn.service.product;


import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductLike;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductLikeRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductLikeService {
    private final ProductLikeRepository productLikeRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public boolean pushInterest(Long productId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ProductLike> productLike = productLikeRepository.findByMemberUsernameAndProductId(username, productId);
        if (productLike.isPresent()) {
            removeInterest(productLike.get().getId());
            return false;
        }
        addInterest(username, productId);
        return true;
    }

    @Transactional
    public void addInterest(String username, Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(AlreadyGoneException::new);
        Member member = memberRepository.findMemberByUsername(username);
        ProductLike productLike = new ProductLike();
        productLike.addMember(member);
        productLike.addProduct(product);
        productLikeRepository.save(productLike);
    }

    @Transactional
    public void removeInterest(Long id) {
        productLikeRepository.deleteById(id);
    }

    public List<ProductLike> findAll() {
        return productLikeRepository.findAll();
    }

    public List<ProductLike> findAllByNickname(String nickname) {
        return productLikeRepository.findAllByMemberNickname(nickname);
    }

}
