package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.area.AreaResponseDto;
import kim.sihwan.daangn.service.area.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/area")
public class AreaController {

    private final AreaService areaService;

    @GetMapping("/{dong}")
    public ResponseEntity<List<AreaResponseDto>> getAreasByDong(@PathVariable String dong){
        System.out.println(dong);
        return new ResponseEntity<>(areaService.getAreasByDong(dong), HttpStatus.OK);
    }

}
