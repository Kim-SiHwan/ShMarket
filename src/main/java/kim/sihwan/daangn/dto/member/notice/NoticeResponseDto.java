package kim.sihwan.daangn.dto.member.notice;

import kim.sihwan.daangn.domain.member.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeResponseDto {

    private final Long id;
    private final Long target;
    private final String type;
    private final String message;
    private final String createDate;
    private final boolean isRead;

    public static NoticeResponseDto toDto(Notice notice){
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .target(notice.getTarget())
                .type(notice.getType())
                .message(notice.getMessage())
                .createDate(notice.getCreateDate().toString())
                .isRead(notice.isChecked())
                .build();
    }
}
