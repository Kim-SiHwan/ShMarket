package kim.sihwan.daangn.service.chat;

import kim.sihwan.daangn.domain.chat.ChatLog;
import kim.sihwan.daangn.domain.chat.ChatRoom;
import kim.sihwan.daangn.dto.chat.ChatLogResponseDto;
import kim.sihwan.daangn.dto.chat.ChatRequestDto;
import kim.sihwan.daangn.repository.chat.ChatLogRepository;
import kim.sihwan.daangn.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final ChatLogRepository chatLogRepository;


    public List<ChatLogResponseDto> findAllChatLogByRoomId(Long roomId){
        return chatLogRepository.findAllByChatRoomId(roomId).stream()
                .map(ChatLogResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long addChatRoom(ChatRequestDto chatRequestDto) {
        String sender = chatRequestDto.getSender();
        String receiver = chatRequestDto.getReceiver();

        ChatRoom chatRoom = ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
        return chatRoomRepository.save(chatRoom).getId();

    }

    @Transactional
    public void addChatLog(ChatRequestDto chatRequestDto) {
        String sender = chatRequestDto.getSender();
        String receiver = chatRequestDto.getReceiver();
        String message = chatRequestDto.getMessage();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRequestDto.getRoomId())
                .orElseThrow(NoSuchElementException::new);

        ChatLog chatLog = ChatLog.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();

        chatLog.addChatRoom(chatRoom);
        chatLogRepository.save(chatLog);

    }

}
