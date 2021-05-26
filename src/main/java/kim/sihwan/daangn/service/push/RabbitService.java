package kim.sihwan.daangn.service.push;

import kim.sihwan.daangn.dto.push.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RabbitService {
    private static final String exchange = "push-exchange";
    private final RabbitTemplate rabbitTemplate;

    public void rabbitProducer(String topic, Long productId) {
        NotificationResponse sendDto = NotificationResponse.builder()
                .topic(topic)
                .productId(productId)
                .build();
        rabbitTemplate.convertAndSend(exchange, "", sendDto);

    }


}
