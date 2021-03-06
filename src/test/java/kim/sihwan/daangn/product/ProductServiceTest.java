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
                .area("??????3???")
                .build();

        product = Product.builder()
                .id(1L)
                .nickname("user")
                .area("??????3???")
                .title("test Title")
                .content("test Content")
                .category("?????????/??????")
                .price("100000")
                .build();

        Set<String> tags = new HashSet<>();
        tags.add("??????");
        tags.add("??????");
        tags.add("?????????");
        requestDto = new ProductRequestDto("??????3???", "test Title", "test Content", "user", "?????????/??????", "100000", "no", null, tags);
    }

    @Test
    @DisplayName("????????? ???????????? ?????????")
    public void ??????_???????????????() throws Exception {

        //given
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
    @DisplayName("1?????? ????????? ???????????? ?????????")
    public void ????????????_???????????????() {

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
    @DisplayName("????????? ???????????? ????????? ??? ?????? ?????? ?????? ?????????")
    public void ??????????????????_???????????????() throws Exception {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ProductResponseDto productResponseDto = productService.findById(product.getId());

        //then

    }

    @Test
    @DisplayName("????????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        List<ProductLike> productLikeList = new ArrayList<>();
        productLikeList.add(new ProductLike());
        productLikeList.add(new ProductLike());

        ArrayList<String> areaList = new ArrayList<>();
        areaList.add("?????????");

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
    @DisplayName("????????? ???????????? ?????????")
    public void ??????_???????????????() throws Exception {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        productService.deleteProduct(product.getId());

        //then
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = NotMineException.class)
    @DisplayName("????????? ????????? ?????? ??????,?????? ????????? ??? ?????? ?????? ?????? ?????????")
    public void ????????????_???????????????() throws Exception {

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
    @DisplayName("????????? ?????? ( SALE, RESERVATION, COMPLETE )??? ???????????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));

        //when
        productService.setStatus(product.getId(), "RESERVATION");

        //then
        assertEquals("RESERVATION", product.getStatus().toString());

    }

}
