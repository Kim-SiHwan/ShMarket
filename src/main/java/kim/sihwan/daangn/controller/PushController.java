package kim.sihwan.daangn.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import kim.sihwan.daangn.dto.keyword.KeywordListResponseDto;
import kim.sihwan.daangn.dto.keyword.KeywordRequestDto;
import kim.sihwan.daangn.service.push.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class PushController {
    private final PushService pushService;

    @PostMapping("/topic")
    public void setTopicFcm(@Valid @RequestBody KeywordRequestDto keywordRequestDto) throws FirebaseMessagingException {
        pushService.addKeyword(keywordRequestDto);
    }

    @GetMapping("/keywords")
    public ResponseEntity<KeywordListResponseDto> getKeywords(String nickname) {
        return new ResponseEntity(pushService.findAllKeywordsByNickname(nickname), HttpStatus.OK);
    }

    @DeleteMapping("/keyword")
    public void removeKeyword(@Valid @RequestBody KeywordRequestDto keywordRequestDto) throws FirebaseMessagingException {
        pushService.deleteKeyword(keywordRequestDto);
    }

}
