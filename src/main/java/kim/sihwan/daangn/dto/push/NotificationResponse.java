package kim.sihwan.daangn.dto.push;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long productId;
    private String topic;
}
