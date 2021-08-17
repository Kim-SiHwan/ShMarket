package kim.sihwan.daangn.qna;

import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.Qna;
import kim.sihwan.daangn.dto.member.qna.QnaRequestDto;
import kim.sihwan.daangn.dto.member.qna.QnaResponseDto;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.member.QnaRepository;
import kim.sihwan.daangn.service.member.QnaService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class QnaServiceTest {

    @InjectMocks
    QnaService qnaService;

    @Mock
    QnaRepository qnaRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("QnaService.addQna Qna 추가 테스트")
    public void Qna_추가테스트() throws Exception {

        //given
        Member member = Member.builder()
                .username("user")
                .build();

        QnaRequestDto qnaRequestDto = QnaRequestDto.builder()
                .title("QnaTitle")
                .content("QnaContent")
                .nickname("user")
                .build();


        given(memberRepository.findMemberByNickname(anyString())).willReturn(Optional.ofNullable(member));


        //when
        qnaService.addQna(qnaRequestDto);

        //then
        verify(qnaRepository, times(1)).save(any(Qna.class));
    }

    @Test
    @DisplayName("QnaSerivce.findById Qna 단일 조회 테스트")
    public void Qna_단일_조회테스트() throws Exception {

        //given
        Qna qna = Qna.builder()
                .id(1L)
                .title("QnaTitle")
                .content("QnaContent")
                .build();

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .build();

        qna.addMember(member);

        given(qnaRepository.findById(anyLong())).willReturn(Optional.ofNullable(qna));

        //when
        QnaResponseDto qnaResponseDto = qnaService.findById(anyLong());

        //then
        assertEquals("QnaTitle", qnaResponseDto.getTitle());
        assertEquals("QnaContent", qnaResponseDto.getContent());
        assertEquals("user", qnaResponseDto.getNickname());

        verify(qnaRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("QnaService.findAllQna Qna 전체 조회 테스트 ")
    public void Qna_전체조회테스트() throws Exception {

        //given
        List<Qna> qnaList = new ArrayList<>();

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .build();

        for (int i = 0; i < 3; i++) {
            Qna qna = Qna.builder()
                    .id((long) i)
                    .title("QnaTitle" + i)
                    .content("QnaContent" + i)
                    .build();
            qna.addMember(member);
            qnaList.add(qna);
        }

        given(qnaRepository.findAll()).willReturn(qnaList);

        //when
        List<QnaResponseDto> list = qnaService.findAllQna();

        //then
        assertEquals(3, list.size());
        //최신순 정렬
        assertEquals("QnaTitle2", list.get(0).getTitle());
        assertEquals("QnaTitle1", list.get(1).getTitle());
        assertEquals("QnaTitle0", list.get(2).getTitle());

    }

}
