package kim.sihwan.daangn.member;


import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.exception.customException.UsernameDuplicatedException;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.service.member.MemberService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MemberServiceTest {


    @Mock
    MemberRepository memberRepository;

    @Mock
    LoginResponseDto loginDto;

    @Mock
    JoinRequestDto joinRequestDto;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    MemberService memberService;


    private Member member = Member
            .builder()
            .username("test")
            .password("testPw")
            .nickname("testNn")
            .area("만수3동")
            .role("ROLE_USER")
            .build();

    private LoginRequestDto loginRequestDto = LoginRequestDto
            .builder()
            .username("test")
            .password("testPw")
            .build();


    @Test
    @DisplayName("정상적으로 가입하는 테스트")
    public void 회원가입_테스트() {

        //given
        given(joinRequestDto.toEntity(joinRequestDto,encoder)).willReturn(member);

        //when
        memberService.join(joinRequestDto);

        //then
        verify(memberRepository,times(1)).save(member);

    }

    @Test(expected = UsernameDuplicatedException.class)
    public void 회원가입_중복_테스트 (){

        //given
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);
        given(joinRequestDto.toEntity(joinRequestDto,encoder)).willReturn(member);
        //when
        memberService.join(joinRequestDto);

        //then
        //중복 ID로 인해 UsernameDuplicatedException 발생

    }


    @Test
    public void 로그인_테스트 (){


    }
}
