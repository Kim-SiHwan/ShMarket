package kim.sihwan.daangn.product;

import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.dto.product.ProductRequestDto;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import kim.sihwan.daangn.service.product.ProductAlbumService;
import kim.sihwan.daangn.service.product.ProductService;
import kim.sihwan.daangn.service.product.ProductTagService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ProductServiceTest {

    @Mock
    Member member;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductAlbumService productAlbumService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductTagService tagService;

    @Mock
    ProductRequestDto productRequestDto;

    @InjectMocks
    ProductService productService;


    private Product product = Product.builder()
            .username("test")
            .nickname("testNn")
            .area("만수3동")
            .content("팝니다")
            .title("파라요")
            .build();


    @Test
    public void 상품등록_테스트() {
        //given
        member = Member.builder()
                .username("test")
                .password("testPw")
                .nickname("testNn")
                .area("만수3동")
                .role("ROLE_USER")
                .build();

        given(productAlbumService.addProductAlbums(productRequestDto)).willReturn(product);
        given(memberRepository.findMemberByNickname(null)).willReturn(Optional.of(member));
        //when


        productService.addProduct(productRequestDto);
        //then
        verify(memberRepository, times(1)).findMemberByNickname(null);
        verify(productRepository, times(1)).save(product);
        verify(tagService, times(1)).addProductTag(eq(product),anySet());


    }

    @Test
    public void 단일상품_조회_테스트 (){

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));

        //when
        Product getProduct = productService.findById(anyLong());

        //then
        verify(productRepository,times(1)).findById(anyLong());
        assertEquals(product.getNickname(),getProduct.getNickname());


    }

    @Test(expected = NoSuchElementException.class)
    public void 없는상품_조회_테스트(){

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        Product getProduct = productService.findById(anyLong());

        //then

    }

    @Test
    public void 상품_조회수_테스트 (){

        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        //when
        Product getProduct = productService.findById(anyLong());

        //then
        assertEquals(1,getProduct.getRead());
    }



}
