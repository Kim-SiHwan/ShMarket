package kim.sihwan.daangn.service.member;

import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.Qna;
import kim.sihwan.daangn.dto.member.qna.QnaRequestDto;
import kim.sihwan.daangn.dto.member.qna.QnaResponseDto;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.member.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long addQna(QnaRequestDto qnaRequestDto){
        Qna qna = qnaRequestDto.toEntity(qnaRequestDto);
        Member member = memberRepository.findById(qnaRequestDto.getMemberId()).orElseThrow(NoSuchElementException::new);
        qna.addMember(member);
        qnaRepository.save(qna);
        return qna.getId();
    }

    public List<QnaResponseDto> findAllQna(){
        return qnaRepository.findAll()
                .stream()
                .map(QnaResponseDto::toDto)
                .sorted(Comparator.comparing(QnaResponseDto::getId, Comparator.reverseOrder())).collect(Collectors.toList());
    }

    public QnaResponseDto findById(Long qnaId){
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NoSuchElementException::new);
        boolean isAdmin = false;
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("ROLE_ADMIN")){
            isAdmin=true;
        }
        if(isAdmin){
            qna.visitedQna();
        }
        return QnaResponseDto.toDto(qna);
    }

    @Transactional
    public void deleteQna(Long id){
        qnaRepository.deleteById(id);
    }

}
