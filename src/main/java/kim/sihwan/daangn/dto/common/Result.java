package kim.sihwan.daangn.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private T data;
    private long totalPage;
}
