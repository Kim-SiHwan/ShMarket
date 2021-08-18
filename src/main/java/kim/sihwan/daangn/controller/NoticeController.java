package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.member.notice.NoticeResponseDto;
import kim.sihwan.daangn.service.member.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "9. Notice")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;


    @ApiOperation(value = "알림 조회", notes = "알림 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getNotices(@RequestParam String nickname) {
        return new ResponseEntity<>(noticeService.getNoticesByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "알림 읽음 상태로 전환", notes = "알림 읽음 상태로 전환")
    @ApiImplicitParam(name = "noticeId", dataType = "Long", value = "변경 대상 PK", required = true)
    @PatchMapping("/{noticeId}")
    public void updateOneIsRead(@PathVariable Long noticeId) {
        noticeService.updateIsRead(noticeId);
    }

    @ApiOperation(value = "알림 전체 읽음 상태로 전환", notes = "알림 전체 읽음 상태로 전환")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @PatchMapping
    public void updateAllIsRead(@RequestParam String nickname) {
        noticeService.updateAllIsRead(nickname);
    }

    @ApiOperation(value = "읽지 않은 알림 수 조회", notes = "읽지 않은 알림 수 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping("/read")
    public ResponseEntity<Integer> getNotReadNotice(@RequestParam String nickname) {
        return new ResponseEntity<>(noticeService.countAllNotReadNotice(nickname), HttpStatus.OK);
    }

}
