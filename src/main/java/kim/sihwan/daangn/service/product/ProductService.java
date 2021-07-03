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
import kim.sihwan.daangn.repository.product.InterestedRepository;
import kim.sihwan.daangn.repository.product.ProductQueryRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import kim.sihwan.daangn.service.push.RabbitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductAlbumService productAlbumService;
    private final MemberRepository memberRepository;
    private final ProductTagService tagService;
    private final ProductInterestedService interestedService;
    private final InterestedRepository interestedRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitService rabbitService;
    private final ProductQueryRepository queryRepository;

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
        List<ProductInterested> interestedList = interestedService.findAll();
        if (isInterested(interestedList, findMemberByUsername().getId(), productId)) {
            return ProductResponseDto.toDto(product, true);
        }
        return ProductResponseDto.toDto(product, false);

    }

    public Result findAllMyProduct(String nickname, int page) {
        Member member = findMemberByUsername();
        Long memberId = member.getId();
        List<ProductInterested> interestedList = interestedService.findAllByNickname(member.getNickname());

        List<ProductListResponseDto> list = queryRepository.findProducts(page, 20, nickname).stream()
                .map(m -> {
                    if (isInterested(interestedList, memberId, m.getId())) {
                        return ProductListResponseDto.toDto(m, true);
                    }
                    return ProductListResponseDto.toDto(m, false);

                })
                .collect(Collectors.toList());
        int totalPage = productRepository.productCountByNickname(nickname) / 20;
        return new Result(list, totalPage);
    }

    public List<ProductListResponseDto> findAllMyLikeProduct(String nickname) {

        List<ProductInterested> interestedList = interestedService.findAllByNickname(nickname);
        return interestedList.stream()
                .map(m -> {
                    return ProductListResponseDto.toDto(m.getProduct(), true);
                }).collect(Collectors.toList());
    }


    public Result paging(int page, List<String> categories, String nickname) {

        Member member = findMemberByUsername();
        Long memberId = member.getId();
        ListOperations<String, String> vo = redisTemplate.opsForList();

        List<String> getCategories = categories.stream()
                .map(m -> URLDecoder.decode(m, StandardCharsets.UTF_8))
                .collect(Collectors.toList());

        List<String> al = vo.range(member.getArea() + "::List", 0L, -1L);

        List<String> blockList = member.getBlocks().stream()
                .map(Block::getToMember)
                .collect(Collectors.toList());

        List<ProductInterested> interestedList = interestedService.findAll();

        List<ProductListResponseDto> list = queryRepository.findProducts(page, 20, nickname).stream()
                .filter(product -> al.contains(product.getArea()) && getCategories.contains(product.getCategory()) && !blockList.contains(product.getNickname()))
                .map(m -> {
                    if (isInterested(interestedList, memberId, m.getId())) {
                        return ProductListResponseDto.toDto(m, true);
                    }
                    return ProductListResponseDto.toDto(m, false);

                })
                .collect(Collectors.toList());
        int totalPage = productRepository.productCount() / 20;

        return new Result(list, totalPage);
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

    public boolean isLike(String username, Long productId) {
        boolean flag = false;
        if (interestedRepository.findByMemberUsernameAndProductId(username, productId).isPresent()) {
            flag = true;
        }
        return flag;
    }

    @Transactional
    public Member findMemberByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findMemberByUsername(username);
    }

}
