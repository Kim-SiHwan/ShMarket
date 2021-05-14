package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.member.qna.QnaRequestDto;
import kim.sihwan.daangn.dto.member.qna.QnaResponseDto;
import kim.sihwan.daangn.service.member.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;

    @GetMapping
    public ResponseEntity<List<QnaResponseDto>> findAllQna(){
        return new ResponseEntity<>(qnaService.findAllQna(), HttpStatus.OK);
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaResponseDto> findById(@PathVariable Long qnaId){
        return new ResponseEntity<>(qnaService.findById(qnaId),HttpStatus.OK);
    }

    @PostMapping
    public void addQna(@RequestBody QnaRequestDto requestDto) {
        qnaService.addQna(requestDto);
    }

    @DeleteMapping("/{qnaId}")
    public void deleteQna(@PathVariable Long qnaId){
        qnaService.deleteQna(qnaId);
    }


}
