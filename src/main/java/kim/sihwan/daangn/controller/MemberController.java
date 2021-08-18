package kim.sihwan.daangn.controller;

import io.swagger.annotations.*;
import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.dto.member.block.BlockDto;
import kim.sihwan.daangn.dto.member.manner.MannerDto;
import kim.sihwan.daangn.dto.member.review.ReviewDto;
import kim.sihwan.daangn.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@Api(tags = "1. Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @ApiOperation(value = "매너평가 조회", notes = "대상의 매너 평가 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "조회 대상 닉네임", required = true)
    @GetMapping("/manner")
    public ResponseEntity<List<MannerDto>> getMannersByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getMannersByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "거래후기 조회", notes = "대상의 거래 후기 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "조회 대상 닉네임", required = true)
    @GetMapping("/review")
    public ResponseEntity<List<ReviewDto>> getReviewsByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getReviewsByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "차단 사용자 조회", notes = "본인이 차단한 사용자 조회")
    @ApiImplicitParam(name = "nickname", dataType = "String", value = "본인 닉네임", required = true)
    @GetMapping("/block")
    public ResponseEntity<List<BlockDto>> getBlocksByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getBlocksByNickname(nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "인증 메일 발송", notes = "인증 메일 발송")
    @ApiImplicitParam(name = "email", dataType = "String", value = "Naver 이메일", required = true)
    @GetMapping("/email")
    public ResponseEntity<String> sendMailToEmail(@RequestParam String email) throws MessagingException {
        UUID emailCode = UUID.randomUUID();
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mail, "UTF-8");
        messageHelper.setFrom(from);
        messageHelper.setTo(email);
        messageHelper.setSubject("Sh Market 회원가입 인증 메일입니다.");
        messageHelper.setText("우측 코드를 입력해주세요 -> " + emailCode);
        javaMailSender.send(mail);
        return new ResponseEntity(emailCode, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 가입", notes = "사용자의 회원 가입")
    @PostMapping("/join")
    public Long join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return memberService.join(joinRequestDto);
    }

    @ApiOperation(value = "로그인", notes = "사용자의 로그인 ( JWT 토큰 발급 ) ")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(memberService.login(loginRequestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "매너평가 추가", notes = "다른 유저의 매너 평가 추가")
    @PostMapping("/manner")
    public void addManner(@Valid @RequestBody MannerDto mannerRequestDto) {
        memberService.addManner(mannerRequestDto);
    }

    @ApiOperation(value = "거래후기 추가", notes = "다른 유저와의 거래 후기 추가")
    @PostMapping("/review")
    public void addReview(@Valid @RequestBody ReviewDto reviewDto) {
        memberService.addReview(reviewDto);
    }

    @ApiOperation(value = "사용자 차단", notes = "다른 사용자를 차단")
    @PostMapping("/block")
    public void addBlock(@RequestBody BlockDto blockDto) {
        memberService.addBlock(blockDto);
    }

    @ApiOperation(value = "사용자 차단 해제", notes = "다른 사용자의 차단을 해제")
    @ApiImplicitParam(name = "blockId", dataType = "Long", value = "차단 해제할 대상 PK", required = true)
    @DeleteMapping("/block/{blockId}")
    public void deleteBlock(@PathVariable Long blockId) {
        memberService.deleteBlock(blockId);
    }

}
