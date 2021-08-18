package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.member.MemberResponseDto;
import kim.sihwan.daangn.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "2. Admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "회원목록 조회", notes = "존재하는 모든 회원 조회")
    @ApiImplicitParam(name = "AUTHORIZATION", value = "Bearer +로그인 후 access_token", required = true, dataType = "String", paramType = "header", defaultValue = "Bearer ")
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findAllMembers(){
        return new ResponseEntity<>(adminService.findAllMembers(),HttpStatus.OK);
    }

    @ApiOperation(value = "Qna 답변 추가",notes = "Qna에 답변 추가")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHORIZATION", value = "Bearer +로그인 후 access_token", required = true, dataType = "String", paramType = "header", defaultValue = "Bearer "),
            @ApiImplicitParam(name = "qnaId", dataType = "Long", value = "Qna 대상 PK", required = true),
            @ApiImplicitParam(name = "answer", dataType = "String", value = "Qna 답변 내용", required = true)
    })
    @PostMapping("/qna/{qnaId}")
    public void addAnswerToQna(@PathVariable Long qnaId,
                               @RequestParam String answer){
        adminService.addAnswer(qnaId, answer);
    }





}
