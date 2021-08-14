package kim.sihwan.daangn.product;

import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductLike;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.dto.product.ProductResponseDto;
import kim.sihwan.daangn.exception.customException.AlreadyGoneException;
import kim.sihwan.daangn.exception.customException.NotMineException;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductLikeRepository;
import kim.sihwan.daangn.repository.product.ProductQueryRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import kim.sihwan.daangn.service.member.MemberService;
import kim.sihwan.daangn.service.product.ProductService;
import kim.sihwan.daangn.service.product.ProductTagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductLikeRepository productLikeRepository;

    @Mock
    ProductQueryRepository queryRepository;

    @Mock
    ProductTagService tagService;

    @Mock
    MemberService memberService;

    @Mock
    ProductRequestDto productRequestDto;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    ListOperations<String, String> listOperations;

    private Product product;

    private ProductRequestDto requestDto;

    private Member member;

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));

        member = Member.builder()
                .username("user")
                .nickname("user")
                .role("ROLE_USER")
                .area("만수3동")
                .build();

        product = Product.builder()
                .id(1L)
                .nickname("user")
                .area("만수3동")
                .title("test Title")
                .content("test Content")
                .category("디지털/가전")
                .price("100000")
                .build();

        Set<String> tags = new HashSet<>();
        tags.add("바지");
        tags.add("신발");
        tags.add("세탁기");
        requestDto = new ProductRequestDto("만수3동", "test Title", "test Content", "user", "디지털/가전", "100000", "no", null, tags);
    }

    @Test
    @DisplayName("상품을 등록하는 테스트")
    public void 상품_등록테스트() throws Exception {

        //given
        given(productRequestDto.toEntity(any(ProductRequestDto.class))).willReturn(product);
        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        productService.addProduct(requestDto);

        //then
        assertEquals(product.getTitle(), requestDto.getTitle());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(memberRepository, times(1)).findMemberByNickname(anyString());
        verify(tagService, times(1)).addProductTag(any(Product.class), anySet());
    }

    @Test
    @DisplayName("1개의 상품을 조회하는 테스트")
    public void 단일상품_조회테스트() {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(productLikeRepository.findByMemberUsernameAndProductId(anyString(), anyLong())).willReturn(Optional.empty());

        //when
        ProductResponseDto productResponseDto = productService.findById(product.getId());

        //then
        assertEquals(productResponseDto.getId(), product.getId());
        assertEquals(productResponseDto.getTitle(), product.getTitle());

        verify(productRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findMemberByUsername(anyString());
        verify(productLikeRepository, times(1)).findByMemberUsernameAndProductId(anyString(), anyLong());
    }

    @Test(expected = AlreadyGoneException.class)
    @DisplayName("삭제된 게시글을 조회할 때 예외 발생 확인 테스트")
    public void 단일상품조회_실패테스트() throws Exception {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ProductResponseDto productResponseDto = productService.findById(product.getId());

        //then

    }

    @Test
    @DisplayName("페이징 테스트")
    public void 전체상품_조회테스트() throws Exception {

        //given
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        List<ProductLike> productLikeList = new ArrayList<>();
        productLikeList.add(new ProductLike());
        productLikeList.add(new ProductLike());

        ArrayList<String> areaList = new ArrayList<>();
        areaList.add("만수동");

        given(redisTemplate.opsForList()).willReturn(listOperations);
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(productLikeRepository.findAllByMemberNickname(anyString())).willReturn(productLikeList);
        given(memberService.setNearArea(anyString())).willReturn(areaList);
        given(queryRepository.findProducts(anyInt(), anyInt(), anyString(), anyList(), anyList(), anyList())).willReturn(productList);

        //when
        Result result = productService.paging(0, new ArrayList<>(), "user");

        //then

        assertEquals(result.getTotalPage(), 1);
        verify(redisTemplate, times(1)).opsForList();
        verify(memberRepository, times(1)).findMemberByUsername(anyString());
        verify(productLikeRepository, times(1)).findAllByMemberNickname(anyString());
        verify(memberService, times(1)).setNearArea(anyString());
        verify(queryRepository, times(1)).findProducts(anyInt(), anyInt(), anyString(), anyList(), anyList(), anyList());

    }

    @Test
    @DisplayName("상품을 삭제하는 테스트")
    public void 상품_삭제테스트() throws Exception {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        productService.deleteProduct(product.getId());

        //then
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = NotMineException.class)
    @DisplayName("타인이 작성한 글에 삭제,수정 요청할 때 예외 발생 확인 테스트")
    public void 상품조작_실패테스트() throws Exception {

        //given
        member = Member.builder()
                .username("guest")
                .build();

        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        productService.deleteProduct(product.getId());

        //then
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("상품의 상태 ( SALE, RESERVATION, COMPLETE )를 수정하는 테스트")
    public void 상품상태_수정테스트() throws Exception {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));

        //when
        productService.setStatus(product.getId(), "RESERVATION");

        //then
        assertEquals("RESERVATION", product.getStatus().toString());

    }

}
