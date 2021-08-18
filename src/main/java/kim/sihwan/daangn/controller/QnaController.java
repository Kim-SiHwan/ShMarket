package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.member.qna.QnaRequestDto;
import kim.sihwan.daangn.dto.member.qna.QnaResponseDto;
import kim.sihwan.daangn.service.member.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "10. Qna")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @ApiOperation(value = "전체 Qna 조회", notes = "전체 Qna 조회")
    @GetMapping
    public ResponseEntity<List<QnaResponseDto>> findAllQna(){
        return new ResponseEntity<>(qnaService.findAllQna(), HttpStatus.OK);
    }

    @ApiOperation(value = "Qna 단일 조회", notes = "Qna 단일 조회")
    @ApiImplicitParam(name = "qnaId", dataType = "Long", value = "조회 대상 PK", required = true)
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaResponseDto> findById(@PathVariable Long qnaId){
        return new ResponseEntity<>(qnaService.findById(qnaId),HttpStatus.OK);
    }

    @ApiOperation(value = "특정 사용자의 Qna 조회", notes = "특정 사용자의 Qna 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "사용자 닉네임", required = true)
    @GetMapping("/my")
    public ResponseEntity<List<QnaResponseDto>> findAllQnaByNickname(@RequestParam String nickname){
        return new ResponseEntity<>(qnaService.findAllQnaByNickname(nickname),HttpStatus.OK);
    }

    @ApiOperation(value = "Qna 추가", notes = "Qna 추가")
    @PostMapping
    public void addQna(@Valid @RequestBody QnaRequestDto requestDto) {
        qnaService.addQna(requestDto);
    }

    @ApiOperation(value = "Qna 삭제", notes = "Qna 삭제")
    @ApiImplicitParam(name = "qnaId", dataType = "Long", value = "삭제 대상 PK", required = true)
    @DeleteMapping("/{qnaId}")
    public void deleteQna(@PathVariable Long qnaId){
        qnaService.deleteQna(qnaId);
    }


}
