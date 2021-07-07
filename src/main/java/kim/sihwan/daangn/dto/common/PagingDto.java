package kim.sihwan.daangn.dto.common;

import kim.sihwan.daangn.domain.member.Block;
import kim.sihwan.daangn.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ListOperations;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PagingDto {

    private final List<String> categories;
    private final List<String> areaList;
    private final List<String> blockList;

    public PagingDto(Member member, ListOperations<String, String> vo, List<String> categories) {
        this.categories = categories.stream()
                .map(m -> URLDecoder.decode(m, StandardCharsets.UTF_8))
                .collect(Collectors.toList());
        this.areaList = vo.range(member.getArea() + "::List", 0L, -1L);
        this.blockList = member.getBlocks().stream()
                .map(Block::getToMember)
                .collect(Collectors.toList());
    }

}
