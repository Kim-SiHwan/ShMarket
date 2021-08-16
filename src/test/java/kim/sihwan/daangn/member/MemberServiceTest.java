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
                .area("만수동")
                .role("ROLE_USER")
                .build();

    }


    @Test
    @DisplayName("MemberService.setNearArea 주변동네 추가 테스트")
    public void 주변동네_추가테스트() throws Exception {

        //given
        Area area = Area.builder()
                .address("인천광역시 남동구 만수동")
                .city("인천광역시")
                .gu("남동구")
                .dong("만수동")
                .lat("37.4561")
                .lng("126.731243")
                .build();

        Area areaForList = Area.builder()
                .address("인천광역시 남동구 만수1동")
                .city("인천광역시")
                .gu("남동구")
                .dong("만수1동")
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
    @DisplayName("MemberService.join 정상적으로 가입하는 테스트")
    public void 회원가입_테스트() throws Exception {

        JoinRequestDto joinDto = JoinRequestDto.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("만수동")
                .build();

        Area area = Area.builder()
                .address("인천광역시 남동구 만수동")
                .city("인천광역시")
                .gu("남동구")
                .dong("만수동")
                .lat("37.4561")
                .lng("126.731243")
                .build();

        Area areaForList = Area.builder()
                .address("인천광역시 남동구 만수1동")
                .city("인천광역시")
                .gu("남동구")
                .dong("만수1동")
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
    @DisplayName("MemberService.join 아이디가 중복되어 예외 발생하는 테스트")
    public void 회원가입_ID_중복테스트() {

        //given
        JoinRequestDto joinDto = JoinRequestDto.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("만수동")
                .build();

        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        memberService.join(joinDto);

        //then
        //중복 ID로 인해 UsernameDuplicatedException 발생

    }

    @Test(expected = NicknameDuplicatedException.class)
    @DisplayName("MemberService.join 닉네임이 중복되어 예외 발생하는 테스트")
    public void 회원가입_닉네임_중복테스트() {

        //given
        JoinRequestDto joinDto = JoinRequestDto.builder()
                .username("user")
                .nickname("user")
                .password("pass")
                .area("만수동")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(java.util.Optional.ofNullable(member));

        //when
        memberService.join(joinDto);

        //then
        //중복 닉네임으로 인해 NicknameDuplicatedException 발생

    }


    @Test
    @DisplayName("MemberService.login 로그인 테스트")
    public void 로그인_테스트() throws Exception {

        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user")
                .password("pass")
                .build();

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .password(passwordEncoder.encode("pass"))
                .area("인천광역시 남동구 만수동")
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
    @DisplayName("MemberService.login 로그인 비밀번호 오류 실패 테스트")
    public void 로그인_실패테스트1() throws Exception {

        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user")
                .password("pass")
                .build();

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .password(passwordEncoder.encode("pass"))
                .area("인천광역시 남동구 만수동")
                .build();

        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).willReturn(false);

        //when
        memberService.login(loginRequestDto);

    }

    @Test(expected = UserNotFoundException.class)
    @DisplayName("MemberService.login 로그인 아이디 오류 실패 테스트")
    public void 로그인_실패테스트2() throws Exception {

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
    @DisplayName("MemberService.addBlock 사용자를 차단하는 테스트")
    public void 사용자_차단테스트() throws Exception {

        //given
        //user가 guest를 차단하는 상황
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
    @DisplayName("MemberService.addBlock 사용자를 이미 차단한 상태에서 다시 차단 요청할 때 예외 발생 테스트")
    public void 사용자차단_중복테스트() throws Exception {

        //given
        //user가 guest를 차단하는 상황
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
    @DisplayName("MemberService.addBlock 5명보다 많은 사용자 차단 요청할 때 예외 발생 테스트")
    public void 사용자차단_초과테스트() throws Exception {

        //given
        //user가 guest를 차단하는 상황
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
    @DisplayName("MemberService.getBlocksByNickname 사용자 차단목록 조회 테스트")
    public void 사용자차단_조회테스트() throws Exception {

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
    @DisplayName("MemberService.deleteBlock 사용자 차단을 해제 테스트")
    public void 사용자차단_삭제테스트() throws Exception {

        //given
        doNothing().when(blockRepository).deleteById(anyLong());

        //when
        memberService.deleteBlock(anyLong());

        //then
        assertEquals(member.getBlocks().size(), 0);
        verify(blockRepository, times(1)).deleteById(anyLong());

    }

    @Test
    @DisplayName("MemberService.addReview 거래후기 등록 테스트")
    public void 거래후기_등록테스트() throws Exception {

        //given
        //user의 물품을 guest가 구매한 상황
        ReviewDto reviewDto = ReviewDto.builder()
                .buyer("guest")
                .nickname("user")
                .content("거래 후기 작성")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        memberService.addReview(reviewDto);

        //then
        assertEquals(member.getReviews().size(), 1);
        verify(memberRepository, times(1)).findMemberByNickname(reviewDto.getNickname());

    }

    @Test
    @DisplayName("MemberService.getReviewsByNickname 거래후기 최신 순 조회 테스트")
    public void 거래후기_조회테스트() throws Exception {

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
        //최신 순으로 정렬
        assertEquals(list.get(0).getBuyer(), "guest2");
        assertEquals(list.get(1).getBuyer(), "guest1");
        assertEquals(list.get(2).getBuyer(), "guest0");
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());
    }

    @Test
    @DisplayName("MemberService.addManner 매너평가 등록 테스트")
    public void 매너평가_등록테스트() throws Exception {

        //given
        //user에 대한 매너 평가 작성
        MannerDto mannerDto = MannerDto.builder()
                .nickname("user")
                .content("매너평가 작성")
                .build();

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        memberService.addManner(mannerDto);

        //then
        assertEquals(member.getManners().size(), 1);
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());
    }

    @Test
    @DisplayName("MemberService.getMannersByNickname 매너평가 조회 테스트")
    public void 매너평가_조회테스트() throws Exception {

        //given
        for (int i = 0; i < 3; i++) {
            Manner manner = Manner.builder()
                    .id((long) i)
                    .content("매너평가" + i)
                    .build();

            manner.addMember(member);
        }

        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));

        //when
        List<MannerDto> list = memberService.getMannersByNickname(member.getNickname());

        //then
        assertEquals(list.size(), 3);
        //최신순 정렬
        assertEquals(list.get(0).getContent(), "매너평가2");
        assertEquals(list.get(1).getContent(), "매너평가1");
        assertEquals(list.get(2).getContent(), "매너평가0");
        verify(memberRepository, times(1)).findMemberByNickname(member.getNickname());

    }
}
