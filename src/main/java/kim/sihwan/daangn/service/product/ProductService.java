package kim.sihwan.daangn.service.product;

import kim.sihwan.daangn.domain.area.SelectedArea;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductInterested;
import kim.sihwan.daangn.dto.product.ProductListResponseDto;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.repository.area.SelectedAreaRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductAlbumService productAlbumService;
    private final MemberRepository memberRepository;
    private final ProductTagService tagService;
    private final SelectedAreaRepository selectedAreaRepository;
    private final ProductInterestedService interestedService;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long addProduct(ProductRequestDto productRequestDto) {
        Product product = productAlbumService.addProductAlbums(productRequestDto);
        Member member = memberRepository.findMemberByNickname(productRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);
        product.addMember(member);
        productRepository.save(product);
        tagService.addProductTag(product, productRequestDto.getTags());
        return product.getId();
    }
    @Transactional
    public void addProduct2(ProductRequestDto productRequestDto) {
        for(int i=0; i<1000; i++){
            Product product = productAlbumService.addProductAlbums(productRequestDto);
            Member member = memberRepository.findMemberByNickname(productRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);
            product.addMember(member);
            productRepository.save(product);
            tagService.addProductTag(product, productRequestDto.getTags());
        }

    }
    @Transactional
    public String setStatus(Long productId,String status){
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

        if(status.equals("SALE")){
            product.setStatusSale();
        }else if(status.equals("RESERVATION")){
            product.setStatusReservation();
        }else{
            product.setStatusCompleted();
        }
        return "상품의 상태가 변경되었습니다.";
    }
    public Product findById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        addRead(productId);
        return product;
    }

    public List<ProductListResponseDto> findAllProductsByCategory(List<String> categories) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findMemberByUsername(username);
        SelectedArea selectedArea = selectedAreaRepository.findByMemberId(member.getId());
        ListOperations<String, String> vo = redisTemplate.opsForList();


        Long memberId = member.getId();

        List<String> al = vo.range(selectedArea.getArea().getAddress() + "::List", 0L, -1L);

        List<ProductInterested> interestedList = interestedService.findAll();

        List<ProductListResponseDto> result = productRepository.findAll()
                .stream()
                .filter(product -> al.contains(product.getArea()) && categories.contains(product.getCategory()))
                .map(m -> {
                    if (isInterested(interestedList, memberId, m.getId())) {
                        return ProductListResponseDto.toDto(m, true);
                    }
                    return ProductListResponseDto.toDto(m, false); //false

                })
                .sorted(Comparator.comparing(ProductListResponseDto::getId, Comparator.reverseOrder())).collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparing(ProductListResponseDto::getId, Comparator.reverseOrder())).collect(Collectors.toList());

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        return result;

    }


    @Transactional
    public void addRead(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        product.addRead();
    }

    public boolean isInterested(List<ProductInterested> interestedList, Long memberId, Long productId) {
        boolean flag = false;
        for (ProductInterested interested : interestedList) {
            if (interested.getMember().getId().equals(memberId) && interested.getProduct().getId().equals(productId)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
