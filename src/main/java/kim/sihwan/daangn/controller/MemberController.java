package kim.sihwan.daangn.controller;

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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @GetMapping("/manner")
    public ResponseEntity<List<MannerDto>> getMannersByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getMannersByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping("/review")
    public ResponseEntity<List<ReviewDto>> getReviewsByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getReviewsByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping("/block")
    public ResponseEntity<List<BlockDto>> getBlocksByNickname(@RequestParam String nickname) {
        return new ResponseEntity<>(memberService.getBlocksByNickname(nickname), HttpStatus.OK);
    }

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

    @PostMapping("/block")
    public void addBlock(@RequestBody BlockDto blockDto) {
        memberService.addBlock(blockDto);
    }

    @DeleteMapping("/block/{blockId}")
    public void deleteBlock(@PathVariable Long blockId) {
        memberService.deleteBlock(blockId);
    }

}
