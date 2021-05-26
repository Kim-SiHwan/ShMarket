package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.keyword.KeywordListResponseDto;
import kim.sihwan.daangn.dto.keyword.KeywordRequestDto;
import kim.sihwan.daangn.service.push.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class PushController {
    private final PushService pushService;

    @PostMapping("/topic")
    public String setTopicFcm(@RequestBody KeywordRequestDto keywordRequestDto) {
        String msg = keywordRequestDto.getKeyword() + "키워드가 등록되었습니다.";
        try {
            pushService.addKeyword(keywordRequestDto);
        } catch (Exception e) {
            msg = "실패";
        }
        return msg;

    }

    @GetMapping("/keywords")
    public ResponseEntity<KeywordListResponseDto> getKeywords() {
        return new ResponseEntity(pushService.findAllKeywordsByUsername(), HttpStatus.OK);
    }

}
