package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.member.notice.NoticeResponseDto;
import kim.sihwan.daangn.service.member.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getNotices(@RequestParam String nickname) {
        return new ResponseEntity<>(noticeService.getNoticesByNickname(nickname), HttpStatus.OK);
    }

    @PatchMapping("/{noticeId}")
    public void updateOneIsRead(@PathVariable Long noticeId) {
        noticeService.updateIsRead(noticeId);
    }

    @PatchMapping
    public void updateAllIsRead(@RequestParam String nickname) {
        noticeService.updateAllIsRead(nickname);
    }

}
