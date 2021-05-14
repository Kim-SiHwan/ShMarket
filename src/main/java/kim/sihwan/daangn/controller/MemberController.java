package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public Long join(@RequestBody JoinRequestDto joinRequestDto){
        return memberService.join(joinRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return new ResponseEntity<>(memberService.login(loginRequestDto), HttpStatus.OK);
    }

}
