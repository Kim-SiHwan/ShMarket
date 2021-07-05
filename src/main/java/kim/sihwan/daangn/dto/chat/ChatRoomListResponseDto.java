package kim.sihwan.daangn.dto.chat;

import kim.sihwan.daangn.domain.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListResponseDto {
    private final Long roomId;
    private final Long productId;
    private final String productTitle;
    private final String productThumbnail;
    private final String productPrice;
    private final String sender;
    private final String receiver;
    private final int notRead;
    private final String lastMessage;
    private final String updateDate;

    public static ChatRoomListResponseDto toDto(ChatRoom chatRoom, int count, String lastMessage) {
        return ChatRoomListResponseDto.builder()
                .roomId(chatRoom.getId())
                .productId(chatRoom.getProduct().getId())
                .productTitle(chatRoom.getProduct().getTitle())
                .productThumbnail(chatRoom.getProduct().getThumbnail())
                .productPrice(chatRoom.getProduct().getPrice())
                .notRead(count)
                .sender(chatRoom.getSender())
                .receiver(chatRoom.getReceiver())
                .lastMessage(lastMessage)
                .updateDate(chatRoom.getUpdateDate().toString())
                .build();
    }
}
