package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "3. Area")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/area")
public class AreaController {

    private final AreaService areaService;

    @ApiOperation(value = "상세 지역 조회", notes = "입력된 값이 포함된 행정동 조회")
    @ApiImplicitParam(name = "dong", dataType = "String", value = "동 이름 EX 만수동 -> 만수 입력", required = true)
    @GetMapping("/{dong}")
    public ResponseEntity<List<AreaResponseDto>> getAreasByDong(@PathVariable String dong) {
        return new ResponseEntity<>(areaService.getAreasByDong(dong), HttpStatus.OK);
    }

}
