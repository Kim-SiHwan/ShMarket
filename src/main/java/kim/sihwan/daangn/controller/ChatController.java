package kim.sihwan.daangn.controller;

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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations msgTemplate;
    private final ChatService chatService;
    private final RabbitService rabbitService;

    @MessageMapping("/chat/msg")
    public void send(@RequestBody @Valid ChatRequestDto chatRequestDto) {
        msgTemplate.convertAndSend("/exchange/chat-exchange/msg." + chatRequestDto.getRoomId(), new ChatLogResponseDto(chatRequestDto));
        rabbitService.rabbitChatProducer(chatRequestDto);
    }

    @PostMapping("/chat")
    public ResponseEntity<Long> addChatRoom(@RequestBody ChatRequestDto chatRequestDto) {
        return new ResponseEntity<>(chatService.addChatRoom(chatRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/chat/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatLogByRoomId(@PathVariable Long roomId,
                                                                  @RequestParam String nickname) {
        return new ResponseEntity<>(chatService.findAllChatLogByRoomId(roomId, nickname), HttpStatus.OK);
    }

    @GetMapping("/chat")
    public ResponseEntity<List<ChatRoomListResponseDto>> getChatRooms(@RequestParam String nickname) {
        return new ResponseEntity<>(chatService.findAllChatRoomByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping("/chat/read")
    public ResponseEntity<Integer> getChatLogNotRead(@RequestParam String nickname) {
        return new ResponseEntity<>(chatService.countAllNotReadChatLog(nickname), HttpStatus.OK);
    }

}
