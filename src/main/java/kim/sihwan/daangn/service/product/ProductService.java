package kim.sihwan.daangn.service.product;

import kim.sihwan.daangn.domain.area.SelectedArea;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductInterested;
import kim.sihwan.daangn.domain.product.ProductTag;
import kim.sihwan.daangn.dto.product.ProductListResponseDto;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.dto.product.ProductResponseDto;
import kim.sihwan.daangn.dto.product.ProductUpdateRequestDto;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.repository.area.SelectedAreaRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import kim.sihwan.daangn.service.push.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
    private final RabbitService rabbitService;

    @Transactional
    public void addProduct(ProductRequestDto productRequestDto) {
        Product product = productRequestDto.toEntity(productRequestDto);
        if (productRequestDto.getHasImages().equals("yes")) {
            product = productAlbumService.addProductAlbums(productRequestDto);
        }

        Member member = memberRepository.findMemberByNickname(productRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);
        product.addMember(member);
        productRepository.save(product);
        tagService.addProductTag(product, productRequestDto.getTags());

        Set<ProductTag> productTags = product.getProductTags();
        productTags.forEach(tags -> rabbitService.rabbitProducer(tags.getTag().getTag(), tags.getProduct().getId()));
    }

    @Transactional
    public ProductResponseDto setStatus(Long productId, String status) {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);

        if (status.equals("SALE")) {
            product.setStatusSale();
        } else if (status.equals("RESERVATION")) {
            product.setStatusReservation();
        } else {
            product.setStatusCompleted();
        }
        return ProductResponseDto.toDto(product,false);
    }

    public ProductResponseDto findById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        addRead(productId);
        List<ProductInterested> interestedList = interestedService.findAll();
        if (isInterested(interestedList, findMemberByUsername().getId(), productId)) {
            return ProductResponseDto.toDto(product, true);
        }
        return ProductResponseDto.toDto(product, false);

    }

    public List<ProductListResponseDto> findAllProductsByCategory(List<String> categories) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Member member = findMemberByUsername();
        SelectedArea selectedArea = selectedAreaRepository.findByMemberId(member.getId());
        ListOperations<String, String> vo = redisTemplate.opsForList();


        List<String> getCategories = categories.stream()
                .map(m -> URLDecoder.decode(m, StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        Long memberId = member.getId();

        List<String> al = vo.range(selectedArea.getArea().getAddress() + "::List", 0L, -1L);

        List<ProductInterested> interestedList = interestedService.findAll();

        List<ProductListResponseDto> result = productRepository.findAll()
                .stream()
                .filter(product -> al.contains(product.getArea()) && getCategories.contains(product.getCategory()))
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
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        String nickname = findMemberByUsername().getNickname();
        if (!product.getNickname().equals(nickname)) {
            throw new NotMineException();
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateRequestDto updateRequestDto) {
        Product product = productRepository.findById(updateRequestDto.getId()).orElseThrow(NoSuchElementException::new);
        String nickname = findMemberByUsername().getNickname();
        if (!product.getNickname().equals(nickname)) {
            throw new NotMineException();
        }
        if (!updateRequestDto.getIds().isEmpty()) {
            productAlbumService.deleteImages(product, updateRequestDto.getIds());
        }
        if (updateRequestDto.getHasImages().equals("yes")) {
            productAlbumService.appendImages(product, updateRequestDto.getFiles());
        }
        product.update(updateRequestDto.getTitle(), updateRequestDto.getContent());
        List<ProductInterested> interestedList = interestedService.findAll();
        return ProductResponseDto.toDto(product, isInterested(interestedList, findMemberByUsername().getId(), product.getId()));
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

    @Transactional
    public Member findMemberByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findMemberByUsername(username);
    }

}
