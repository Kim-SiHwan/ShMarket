package kim.sihwan.daangn.service.push;

import com.fasterxml.jackson.databind.ObjectMapper;
import kim.sihwan.daangn.dto.push.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RabbitConsumer {
    private static final String queues = "push-queue";
    private final PushService pushService;

    @RabbitListener(queues = queues, concurrency = "6")
    public void pushConsumer(final Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        NotificationResponse notificationResponse = objectMapper.readValue(message.getBody(), NotificationResponse.class);
        System.out.println(notificationResponse.getTopic() + " " + notificationResponse.getProductId());

        pushService.sendTopic(notificationResponse);
    }
}
