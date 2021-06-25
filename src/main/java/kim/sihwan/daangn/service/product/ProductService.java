package kim.sihwan.daangn.service.product;

import kim.sihwan.daangn.domain.member.Block;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductInterested;
import kim.sihwan.daangn.domain.product.ProductTag;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.dto.product.ProductListResponseDto;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.dto.product.ProductResponseDto;
import kim.sihwan.daangn.dto.product.ProductUpdateRequestDto;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.exception.customException.OverSizeException;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import kim.sihwan.daangn.service.push.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
    private final ProductInterestedService interestedService;
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitService rabbitService;

    @Transactional
    public void addProduct(ProductRequestDto productRequestDto) {
        Product product = productRequestDto.toEntity(productRequestDto);
        if (productRequestDto.getTags().size() > 3) {
            throw new OverSizeException("tag");
        }

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
        Product product = productRepository.findById(productId).orElseThrow(AlreadyGoneException::new);

        if (status.equals("SALE")) {
            product.setStatusSale();
        } else if (status.equals("RESERVATION")) {
            product.setStatusReservation();
        } else {
            product.setStatusCompleted();
        }
        return ProductResponseDto.toDto(product, false);
    }

    public ProductResponseDto findById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(AlreadyGoneException::new);
        addRead(productId);
        List<ProductInterested> interestedList = interestedService.findAll();
        if (isInterested(interestedList, findMemberByUsername().getId(), productId)) {
            return ProductResponseDto.toDto(product, true);
        }
        return ProductResponseDto.toDto(product, false);

    }

    public List<ProductListResponseDto> findAllMyProduct(String originName, String requestNickname) {
        List<ProductInterested> interestedList = interestedService.findAllByNickname(originName);
        Member member = memberRepository.findMemberByNickname(originName).orElseThrow(NoSuchElementException::new);
        return productRepository.findAllByMemberNickname(requestNickname).stream()
                .map(m -> {
                    if (isInterested(interestedList, member.getId(), m.getId())) {
                        return ProductListResponseDto.toDto(m, true);
                    }
                    return ProductListResponseDto.toDto(m, false);
                }).collect(Collectors.toList());
    }

    public List<ProductListResponseDto> findAllMyLikeProduct(String nickname) {

        List<ProductInterested> interestedList = interestedService.findAllByNickname(nickname);
        return interestedList.stream()
                .map(m -> {
                    return ProductListResponseDto.toDto(m.getProduct(), true);
                }).collect(Collectors.toList());
    }

    public Result paging(int offset, List<String> categories) {

        Member member = findMemberByUsername();
        Long memberId = member.getId();

        ListOperations<String, String> vo = redisTemplate.opsForList();

        List<String> getCategories = categories.stream()
                .map(m -> URLDecoder.decode(m, StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        List<String> al = vo.range(member.getArea() + "::List", 0L, -1L);

        List<ProductInterested> interestedList = interestedService.findAll();

        List<String> blockList = member.getBlocks().stream()
                .map(Block::getToMember)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(offset, 20);
        Slice<Product> page = productRepository.findProducts(pageable, LocalDateTime.now());

        List<ProductListResponseDto> result = page
                .stream()
                .filter(product -> al.contains(product.getArea()) && getCategories.contains(product.getCategory()) && !blockList.contains(product.getNickname()))
                .map(m -> {
                    if (isInterested(interestedList, memberId, m.getId())) {
                        return ProductListResponseDto.toDto(m, true);
                    }
                    return ProductListResponseDto.toDto(m, false);

                })
                .collect(Collectors.toList());

        return new Result(result, page.hasNext());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(AlreadyGoneException::new);
        String nickname = findMemberByUsername().getNickname();
        if (!product.getNickname().equals(nickname)) {
            throw new NotMineException();
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateRequestDto updateRequestDto) {
        Product product = productRepository.findById(updateRequestDto.getId()).orElseThrow(AlreadyGoneException::new);
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
        Product product = productRepository.findById(productId).orElseThrow(AlreadyGoneException::new);
        product.addRead();
    }

    public boolean isInterested(List<ProductInterested> interestedList, Long memberId, Long productId) {
        boolean flag = false;

        //효율성 올려야함
        //이 전의 것으로 하면 쿼리가 엄청 나가고 이렇게 하면 반복문이 너무 많이 돔 해결책 강구
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
