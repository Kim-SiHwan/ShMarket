package kim.sihwan.daangn.dto.member.notice;

import kim.sihwan.daangn.domain.member.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeResponseDto {

    private Long id;
    private Long target;
    private String type;
    private String message;
    private String createDate;
    private boolean isRead;

    public static NoticeResponseDto toDto(Notice notice){
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .target(notice.getTarget())
                .type(notice.getType())
                .message(notice.getMessage())
                .createDate(notice.getCreateDate().toString())
                .isRead(notice.isRead())
                .build();
    }
}
