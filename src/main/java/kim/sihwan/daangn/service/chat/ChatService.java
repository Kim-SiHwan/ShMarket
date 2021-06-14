package kim.sihwan.daangn.service.chat;

import kim.sihwan.daangn.domain.chat.ChatLog;
import kim.sihwan.daangn.domain.chat.ChatRoom;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.dto.chat.ChatLogResponseDto;
import kim.sihwan.daangn.dto.chat.ChatRequestDto;
import kim.sihwan.daangn.dto.chat.ChatRoomResponseDto;
import kim.sihwan.daangn.repository.chat.ChatLogRepository;
import kim.sihwan.daangn.repository.chat.ChatRoomRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
    private final ProductRepository productRepository;

    @Transactional
    public List<ChatLogResponseDto> findAllChatLogByRoomId(Long roomId, String nickname) {
        changeChatLogAllRead(roomId, nickname);
        return chatLogRepository.findAllByChatRoomId(roomId).stream()
                .map(ChatLogResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public int findAllChatLogByRoomIdAndNotRead(Long roomId, String nickname) {
        return chatLogRepository.countAllByChatRoomIdAndReadAndReceiver(roomId, false, nickname);
    }

    public int countAllNotReadChatLog(String nickname) {
        return chatLogRepository.countAllByReceiverAndRead(nickname, false);
    }

    @Transactional
    public String findLastMessage(Long roomId) {
        ChatLog chatLog = chatLogRepository.findTop1ByChatRoomIdOrderByIdDesc(roomId);
        return chatLog.getMessage();
    }

    @Transactional
    public void changeChatLogAllRead(Long roomId, String nickname) {
        List<ChatLog> list = chatLogRepository.findAllByChatRoomIdAndReadAndReceiver(roomId, false, nickname);
        list.forEach(ChatLog::changeReadState);
    }

    public List<ChatRoomResponseDto> findAllChatRoomByNickname(String nickname) {
        return chatRoomRepository.findAllBySenderOrReceiver(nickname, nickname).stream()
                .map(m -> {
                    int count = findAllChatLogByRoomIdAndNotRead(m.getId(), nickname);
                    String lasMessage = findLastMessage(m.getId());
                    return ChatRoomResponseDto.toDto(m, count, lasMessage);
                })
                .sorted(Comparator.comparing(ChatRoomResponseDto::getUpdateDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long addChatRoom(ChatRequestDto chatRequestDto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .sender(chatRequestDto.getSender())
                .receiver(chatRequestDto.getReceiver())
                .build();

        Product product = productRepository.findById(chatRequestDto.getProductId()).orElseThrow(NoSuchElementException::new);
        chatRoom.addProduct(product);
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
        chatRoom.changeDate();
        chatLogRepository.save(chatLog);
    }


}
