package kim.sihwan.daangn.dto.chat;

import kim.sihwan.daangn.domain.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ChatRoomResponseDto {
    private final Long chatRoomId;
    private final Long productId;
    private final String productTitle;
    private final String productThumbnail;
    private final String productPrice;
    private final List<ChatLogResponseDto> chatLogs;

    public static ChatRoomResponseDto toDto(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .productId(chatRoom.getProduct().getId())
                .productTitle(chatRoom.getProduct().getTitle())
                .productThumbnail(chatRoom.getProduct().getThumbnail())
                .productPrice(chatRoom.getProduct().getPrice())
                .chatLogs(chatRoom.getChatLogs().stream()
                        .map(ChatLogResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
