package kim.sihwan.daangn.service.product;


import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductInterested;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.InterestedRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductInterestedService {
    private final InterestedRepository interestedRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    @Transactional
    public String pushInterest(Long productId){
        String msg= "존재하지 않는 값이라 추가했음";
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ProductInterested interested = interestedRepository.findByMemberUsernameAndProductId(username,productId);
        if(interested != null){
            msg="이미 존재하는 값이라 삭제했음";
            removeInterest(interested.getId());
            return msg;
        }
        addInterest(username,productId);
        return msg;
    }

    @Transactional
    public void addInterest(String username,Long productId){

        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        Member member = memberRepository.findMemberByUsername(username);
        ProductInterested productInterested = new ProductInterested();
        productInterested.addMember(member);
        productInterested.addProduct(product);
        interestedRepository.save(productInterested);
    }

    @Transactional
    public void removeInterest(Long id){
        interestedRepository.deleteById(id);
    }


    public List<ProductInterested> findAll(){
        return interestedRepository.findAll();
    }

    public void test(){
        for(int i=1; i<=1000; i++){
            String username = "user";
            ProductInterested interested = interestedRepository.findByMemberUsernameAndProductId(username,(long)i);
            if(interested != null){
                removeInterest(interested.getId());
            }
            addInterest(username,(long)i);
        }

    }



}
