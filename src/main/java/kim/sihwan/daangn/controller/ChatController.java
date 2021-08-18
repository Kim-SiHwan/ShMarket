package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.chat.ChatLogResponseDto;
import kim.sihwan.daangn.dto.chat.ChatRequestDto;
import kim.sihwan.daangn.dto.chat.ChatRoomListResponseDto;
import kim.sihwan.daangn.dto.chat.ChatRoomResponseDto;
import kim.sihwan.daangn.service.chat.ChatService;
import kim.sihwan.daangn.service.push.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "7. Chat")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations msgTemplate;
    private final ChatService chatService;
    private final RabbitService rabbitService;

    @ApiOperation(value = "채팅 전송", notes = "채팅 전송")
    @MessageMapping("/chat/msg")
    public void send(@RequestBody @Valid ChatRequestDto chatRequestDto) {
        msgTemplate.convertAndSend("/exchange/chat-exchange/msg." + chatRequestDto.getRoomId(), new ChatLogResponseDto(chatRequestDto));
        rabbitService.rabbitChatProducer(chatRequestDto);
    }

    @ApiOperation(value = "채팅방 생성", notes = "채팅방 생성")
    @PostMapping("/chat")
    public ResponseEntity<Long> addChatRoom(@RequestBody ChatRequestDto chatRequestDto) {
        return new ResponseEntity<>(chatService.addChatRoom(chatRequestDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "채팅내역 조회", notes = "채팅방의 채팅 내역 조회")
    @ApiImplicitParams
            ({
                    @ApiImplicitParam(name = "roomId", dataType = "Long", value = "채팅방 PK", required = true),
                    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
            })
    @GetMapping("/chat/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatLogByRoomId(@PathVariable Long roomId,
                                                                  @RequestParam String nickname) {
        return new ResponseEntity<>(chatService.findAllChatLogByRoomId(roomId, nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "채팅방 전체 조회", notes = "채팅방 전체 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping("/chat")
    public ResponseEntity<List<ChatRoomListResponseDto>> getChatRooms(@RequestParam String nickname) {
        return new ResponseEntity<>(chatService.findAllChatRoomByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "읽지 않은 채팅 수 조회", notes = "읽지 않은 채팅 수 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping("/chat/read")
    public ResponseEntity<Integer> getChatLogNotRead(@RequestParam String nickname) {
        return new ResponseEntity<>(chatService.countAllNotReadChatLog(nickname), HttpStatus.OK);
    }

}
