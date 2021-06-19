package kim.sihwan.daangn.dto.member.block;

import kim.sihwan.daangn.domain.member.Block;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class BlockDto {

    private Long id;

    @NotBlank(message = "사용자의 닉네임은 필수 항목입니다.")
    private String fromNickname;

    @NotBlank(message = "상대방의 닉네임은 필수 항목입니다.")
    private String toNickname;

    public Block toEntity(BlockDto blockDto){
        return Block.builder()
                .toMember(blockDto.getToNickname())
                .build();
    }

    public static BlockDto toDto(Block block){
        return BlockDto.builder()
                .id(block.getId())
                .toNickname(block.getMember().getNickname())
                .build();
    }

}
