package kim.sihwan.daangn.service.admin;

import kim.sihwan.daangn.domain.member.Qna;
import kim.sihwan.daangn.dto.member.MemberResponseDto;
import kim.sihwan.daangn.exception.customException.ForbiddenAccessException;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.member.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    public boolean isAdmin() {
        for (GrantedAuthority auth : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

    public List<MemberResponseDto> findAllMembers() {
        if (!isAdmin()) {
            throw new ForbiddenAccessException();
        }
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addAnswer(Long qnaId, String answer) {
        if (!isAdmin()) {
            throw new ForbiddenAccessException();
        }
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NoSuchElementException::new);
        qna.answeredQna(answer);

    }
}
