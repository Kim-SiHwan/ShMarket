package kim.sihwan.daangn.member;


import kim.sihwan.daangn.config.jwt.JwtTokenProvider;
import kim.sihwan.daangn.domain.area.Area;
import kim.sihwan.daangn.domain.member.Block;
import kim.sihwan.daangn.domain.member.Manner;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.Review;
import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.dto.member.block.BlockDto;
import kim.sihwan.daangn.dto.member.manner.MannerDto;
import kim.sihwan.daangn.dto.member.review.ReviewDto;
import kim.sihwan.daangn.exception.customException.*;
import kim.sihwan.daangn.repository.area.AreaRepository;
import kim.sihwan.daangn.repository.member.BlockRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.service.member.MemberService;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AreaRepository areaRepository;

    @Mock
    BlockRepository blockRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JoinRequestDto joinRequestDto;

    @Mock
    JwtTokenProvider tokenProvider;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    ListOperations<String, String> listOperations;

    private Member member;

    @Before
    public void setUp() {


        member = Member.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("?????????")
                .role("ROLE_USER")
                .build();

    }


    @Test
    @DisplayName("MemberService.setNearArea ???????????? ?????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        Area area = Area.builder()
                .address("??????????????? ????????? ?????????")
                .city("???????????????")
                .gu("?????????")
                .dong("?????????")
                .lat("37.4561")
                .lng("126.731243")
                .build();

        Area areaForList = Area.builder()
                .address("??????????????? ????????? ??????1???")
                .city("???????????????")
                .gu("?????????")
                .dong("??????1???")
                .lat("37.448877")
                .lng("126.732117")
                .build();

        List<Area> areaList = new ArrayList<>();
        areaList.add(area);
        areaList.add(areaForList);

        given(areaRepository.findAllByCityLike(anyString())).willReturn(areaList);
        given(areaRepository.findByAddress(anyString())).willReturn(area);
        given(redisTemplate.opsForList()).willReturn(listOperations);

        //when
        ArrayList<String> nearAreaList = memberService.setNearArea(anyString());

        //then
        assertEquals(nearAreaList.size(), 2);
        verify(areaRepository, times(1)).findAllByCityLike(anyString());
        verify(areaRepository, times(1)).findByAddress(anyString());

    }

    @Test
    @DisplayName("MemberService.join ??????????????? ???????????? ?????????")
    public void ????????????_?????????() throws Exception {

        JoinRequestDto joinDto = JoinRequestDto.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("?????????")
                .build();

        Area area = Area.builder()
                .address("??????????????? ????????? ?????????")
                .city("???????????????")
                .gu("?????????")
                .dong("?????????")
                .lat("37.4561")
                .lng("126.731243")
                .build();

        Area areaForList = Area.builder()
                .address("??????????????? ????????? ??????1???")
                .city("???????????????")
                .gu("?????????")
                .dong("??????1???")
                .lat("37.448877")
                .lng("126.732117")
                .build();

        List<Area> areaList = new ArrayList<>();
        areaList.add(area);
        areaList.add(areaForList);

        //given
        given(areaRepository.findAllByCityLike(anyString())).willReturn(areaList);
        given(areaRepository.findByAddress(anyString())).willReturn(area);
        given(redisTemplate.opsForList()).willReturn(listOperations);

        //when
        Long memberId = memberService.join(joinDto);

        //then
        verify(memberRepository, times(1)).save(any(Member.class));

    }

    @Test(expected = UsernameDuplicatedException.class)
    @DisplayName("MemberService.join ???????????? ???????????? ?????? ???????????? ?????????")
    public void ????????????_ID_???????????????() {

        //given
        JoinRequestDto joinDto = JoinRequestDto.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("?????????")
                .build();

        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        memberService.join(joinDto);

        //then
        //?????? ID??? ?????? UsernameDuplicatedException ??????

    }

    @Test(expected = NicknameDuplicatedException.class)
    @DisplayName("MemberService.join ???????????? ???????????? ?????? ???????????? ?????????")
    public void ????????????_?????????_???????????????() {

        //given
        JoinRequestDto joinDto = JoinRequestDto.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("?????????")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(java.util.Optional.ofNullable(member));

        //when
        memberService.join(joinDto);

        //then
        //?????? ??????????????? ?????? NicknameDuplicatedException ??????

    }


    @Test
    @DisplayName("MemberService.login ????????? ?????????")
    public void ?????????_?????????() throws Exception {

        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user")
                .password("pass")
                .build();

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .password(passwordEncoder.encode("pass"))
                .area("??????????????? ????????? ?????????")
                .build();

        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).willReturn(true);
        given(tokenProvider.createToken(anyString())).willReturn("jwt");

        //when
        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        //then
        assertEquals(loginResponseDto.getToken(), "jwt");
        assertEquals(loginResponseDto.getUsername(), "user");
        verify(memberRepository, times(1)).findMemberByUsername(loginRequestDto.getUsername());

    }

    @Test(expected = BadCredentialsException.class)
    @DisplayName("MemberService.login ????????? ???????????? ?????? ?????? ?????????")
    public void ?????????_???????????????1() throws Exception {

        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user")
                .password("pass")
                .build();

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .password(passwordEncoder.encode("pass"))
                .area("??????????????? ????????? ?????????")
                .build();

        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).willReturn(false);

        //when
        memberService.login(loginRequestDto);

    }

    @Test(expected = UserNotFoundException.class)
    @DisplayName("MemberService.login ????????? ????????? ?????? ?????? ?????????")
    public void ?????????_???????????????2() throws Exception {

        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user")
                .password("pass")
                .build();

        given(memberRepository.findMemberByUsername(anyString())).willReturn(null);

        //when
        memberService.login(loginRequestDto);

    }

    @Test
    @DisplayName("MemberService.addBlock ???????????? ???????????? ?????????")
    public void ?????????_???????????????() throws Exception {

        //given
        //user??? guest??? ???????????? ??????
        BlockDto blockRequestDto = BlockDto.builder()
                .toNickname("guest")
                .fromNickname("user")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));
        given(blockRepository.findByMemberNicknameAndToMember(anyString(), anyString())).willReturn(Optional.empty());

        //when
        memberService.addBlock(blockRequestDto);

        //then
        assertEquals(member.getBlocks().size(), 1);
        verify(memberRepository, times(1)).findMemberByNickname(blockRequestDto.getFromNickname());
        verify(blockRepository, times(1)).findByMemberNicknameAndToMember(blockRequestDto.getFromNickname(), blockRequestDto.getToNickname());

    }

    @Test(expected = AlreadyExistException.class)
    @DisplayName("MemberService.addBlock ???????????? ?????? ????????? ???????????? ?????? ?????? ????????? ??? ?????? ?????? ?????????")
    public void ???????????????_???????????????() throws Exception {

        //given
        //user??? guest??? ???????????? ??????
        BlockDto blockRequestDto = BlockDto.builder()
                .toNickname("guest")
                .fromNickname("user")
                .build();

        Block block = Block.builder()
                .toMember("guest")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));
        given(blockRepository.findByMemberNicknameAndToMember(anyString(), anyString())).willReturn(Optional.ofNullable(block));

        //when
        memberService.addBlock(blockRequestDto);

        //then

    }

    @Test(expected = OverSizeException.class)
    @DisplayName("MemberService.addBlock 5????????? ?????? ????????? ?????? ????????? ??? ?????? ?????? ?????????")
    public void ???????????????_???????????????() throws Exception {

        //given
        //user??? guest??? ???????????? ??????
        BlockDto blockRequestDto = BlockDto.builder()
                .toNickname("guest")
                .fromNickname("user")
                .build();

        member = Member.builder()
                .username("user")
                .nickname("user")
                .build();

        for (int i = 0; i < 5; i++) {
            Block block = Block.builder()
                    .toMember("guest" + i)
                    .build();

            block.addMember(member);
        }

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));
        given(blockRepository.findByMemberNicknameAndToMember(anyString(), anyString())).willReturn(Optional.empty());

        //when
        memberService.addBlock(blockRequestDto);

        //then

    }

    @Test
    @DisplayName("MemberService.getBlocksByNickname ????????? ???????????? ?????? ?????????")
    public void ???????????????_???????????????() throws Exception {

        //given
        for (int i = 0; i < 5; i++) {
            Block block = Block.builder()
                    .toMember("guest" + i)
                    .build();

            block.addMember(member);
        }
        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        List<BlockDto> list = memberService.getBlocksByNickname(member.getNickname());

        //then
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());
        assertEquals(list.size(), 5);
        assertEquals(list.get(0).getToNickname(), "guest0");
        assertEquals(list.get(4).getToNickname(), "guest4");

    }

    @Test
    @DisplayName("MemberService.deleteBlock ????????? ????????? ?????? ?????????")
    public void ???????????????_???????????????() throws Exception {

        //given
        doNothing().when(blockRepository).deleteById(anyLong());

        //when
        memberService.deleteBlock(anyLong());

        //then
        assertEquals(member.getBlocks().size(), 0);
        verify(blockRepository, times(1)).deleteById(anyLong());

    }

    @Test
    @DisplayName("MemberService.addReview ???????????? ?????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        //user??? ????????? guest??? ????????? ??????
        ReviewDto reviewDto = ReviewDto.builder()
                .buyer("guest")
                .nickname("user")
                .content("?????? ?????? ??????")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        memberService.addReview(reviewDto);

        //then
        assertEquals(member.getReviews().size(), 1);
        verify(memberRepository, times(1)).findMemberByNickname(reviewDto.getNickname());

    }

    @Test
    @DisplayName("MemberService.getReviewsByNickname ???????????? ?????? ??? ?????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        for (int i = 0; i < 3; i++) {
            Review review = Review.builder()
                    .id((long) (i + 1))
                    .buyer("guest" + i)
                    .build();
            review.addMember(member);
        }
        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        List<ReviewDto> list = memberService.getReviewsByNickname(member.getNickname());

        //then
        assertEquals(list.size(), 3);
        //?????? ????????? ??????
        assertEquals(list.get(0).getBuyer(), "guest2");
        assertEquals(list.get(1).getBuyer(), "guest1");
        assertEquals(list.get(2).getBuyer(), "guest0");
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());
    }

    @Test
    @DisplayName("MemberService.addManner ???????????? ?????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        //user??? ?????? ?????? ?????? ??????
        MannerDto mannerDto = MannerDto.builder()
                .nickname("user")
                .content("???????????? ??????")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        memberService.addManner(mannerDto);

        //then
        assertEquals(member.getManners().size(), 1);
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());
    }

    @Test
    @DisplayName("MemberService.getMannersByNickname ???????????? ?????? ?????????")
    public void ????????????_???????????????() throws Exception {

        //given
        for (int i = 0; i < 3; i++) {
            Manner manner = Manner.builder()
                    .id((long) i)
                    .content("????????????" + i)
                    .build();

            manner.addMember(member);
        }

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        List<MannerDto> list = memberService.getMannersByNickname(member.getNickname());

        //then
        assertEquals(list.size(), 3);
        //????????? ??????
        assertEquals(list.get(0).getContent(), "????????????2");
        assertEquals(list.get(1).getContent(), "????????????1");
        assertEquals(list.get(2).getContent(), "????????????0");
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());

    }
}
