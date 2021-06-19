package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.dto.member.block.BlockDto;
import kim.sihwan.daangn.dto.member.manner.MannerDto;
import kim.sihwan.daangn.dto.member.review.ReviewDto;
import kim.sihwan.daangn.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return memberService.join(joinRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(memberService.login(loginRequestDto), HttpStatus.OK);
    }

    @PostMapping("/manner")
    public void addManner(@Valid @RequestBody MannerDto mannerRequestDto) {
        memberService.addManner(mannerRequestDto);
    }

    @PostMapping("/review")
    public void addReview(@Valid @RequestBody ReviewDto reviewDto) {
        memberService.addReview(reviewDto);
    }

    @GetMapping("/manner")
    public ResponseEntity<List<MannerDto>> getMannersByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getMannersByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping("/review")
    public ResponseEntity<List<ReviewDto>> getReviewsByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getReviewsByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping("/block")
    public ResponseEntity<List<BlockDto>> getBlocksByNickname(@RequestParam String nickname){
        return new ResponseEntity<>(memberService.getBlocksByNickname(nickname), HttpStatus.OK);
    }

    @PostMapping("/block")
    public void addBlock(@RequestBody BlockDto blockDto){
        memberService.addBlock(blockDto);
    }

    @DeleteMapping("/block/{blockId}")
    public void deleteBlock(@PathVariable Long blockId){
        memberService.deleteBlock(blockId);
    }


}
