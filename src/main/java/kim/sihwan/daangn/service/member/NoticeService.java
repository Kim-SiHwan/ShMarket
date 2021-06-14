package kim.sihwan.daangn.service.member;

import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.Notice;
import kim.sihwan.daangn.dto.board.BoardListResponseDto;
import kim.sihwan.daangn.dto.member.notice.NoticeResponseDto;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.member.NoticeRepository;
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
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public String getNickname() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findMemberByUsername(username);
        System.out.println(member.getNickname());
        return member.getNickname();
    }

    @Transactional
    public void addNotice(Notice notice, String nickname) {
        Member member = memberRepository.findMemberByNickname(nickname)
                .orElseThrow(NoSuchElementException::new);
        notice.addMember(member);
        noticeRepository.save(notice);
    }

    public List<NoticeResponseDto> getNoticesByNickname(String nickname) {
        return noticeRepository.findAllByMemberNickname(nickname).stream()
                .map(NoticeResponseDto::toDto)
                .sorted(Comparator.comparing(NoticeResponseDto::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateIsRead(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NoSuchElementException::new);
        notice.updateRead();
    }

    @Transactional
    public void updateAllIsRead(String nickname) {
        List<Notice> result = noticeRepository.findAllByMemberNickname(nickname);
        result.forEach(Notice::updateRead);
    }

}
