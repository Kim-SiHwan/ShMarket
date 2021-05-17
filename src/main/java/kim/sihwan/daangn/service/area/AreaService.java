package kim.sihwan.daangn.service.area;

import kim.sihwan.daangn.dto.area.AreaResponseDto;
import kim.sihwan.daangn.repository.area.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaService {
    private final AreaRepository areaRepository;

    public List<AreaResponseDto> getAreasByDong(String dong){
        System.out.println(dong+"검색");
        return areaRepository.findAllByDongLike("%"+dong+"%")
                .stream()
                .map(AreaResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
