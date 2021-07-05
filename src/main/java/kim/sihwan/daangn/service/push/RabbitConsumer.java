package kim.sihwan.daangn.service.push;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.sihwan.daangn.dto.chat.ChatRequestDto;
import kim.sihwan.daangn.dto.push.NotificationResponse;
import kim.sihwan.daangn.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitConsumer {
    private final PushService pushService;
    private final ChatService chatService;

    @RabbitListener(queues = "keyword-queue", concurrency = "6")
    public void pushConsumer(final Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        NotificationResponse notificationResponse = objectMapper.readValue(message.getBody(), NotificationResponse.class);
        pushService.sendTopic(notificationResponse);

    }

    @RabbitListener(queues = "chat-queue", concurrency = "6")
    public void pushChatConsumer(final Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRequestDto chatRequestDto = objectMapper.readValue(message.getBody(), ChatRequestDto.class);
        pushService.sendByToken(chatRequestDto);
        chatService.addChatLog(chatRequestDto);
    }
}
