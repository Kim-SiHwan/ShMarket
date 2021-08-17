package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.member.MemberResponseDto;
import kim.sihwan.daangn.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findAllMembers(){
        return new ResponseEntity<>(adminService.findAllMembers(),HttpStatus.OK);
    }

    @PostMapping("/qna/{qnaId}")
    public void addAnswerToQna(@PathVariable Long qnaId,
                               @RequestParam String answer){
        adminService.addAnswer(qnaId, answer);
    }





}
