package kim.sihwan.daangn.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.keyword.KeywordListResponseDto;
import kim.sihwan.daangn.dto.keyword.KeywordRequestDto;
import kim.sihwan.daangn.service.push.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "8. Push")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class PushController {
    private final PushService pushService;

    @ApiOperation(value = "키워드 추가", notes = "키워드, FCM 토큰을 추가")
    @PostMapping("/topic")
    public void setTopicFcm(@Valid @RequestBody KeywordRequestDto keywordRequestDto) throws FirebaseMessagingException {
        pushService.addKeyword(keywordRequestDto);
    }


    @ApiOperation(value = "키워드 조회", notes = "키워드 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping("/keywords")
    public ResponseEntity<KeywordListResponseDto> getKeywords(String nickname) {
        return new ResponseEntity(pushService.findAllKeywordsByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "키워드 삭제", notes = "키워드 삭제")
    @DeleteMapping("/keyword")
    public void removeKeyword(@Valid @RequestBody KeywordRequestDto keywordRequestDto) throws FirebaseMessagingException {
        pushService.deleteKeyword(keywordRequestDto);
    }

}
