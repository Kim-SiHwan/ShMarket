package kim.sihwan.daangn.controller;

import kim.sihwan.daangn.dto.product.ProductListResponseDto;
import kim.sihwan.daangn.service.member.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    @GetMapping
    public ResponseEntity<List<ProductListResponseDto>> getProductsOfFollowings(@RequestParam String nickname) {
        return new ResponseEntity<>(followService.getProductsOfFollowings(nickname), HttpStatus.OK);
    }

    @GetMapping("/following")
    public ResponseEntity<List<String>> getFollowings(@RequestParam String nickname) {
        return new ResponseEntity<>(followService.getFollowings(nickname), HttpStatus.OK);
    }

    @PostMapping
    public void addFollowing(@RequestParam String fromMember,
                             @RequestParam String toMember) {
        followService.addFollow(fromMember, toMember);
    }

    @DeleteMapping
    public void unFollowing(@RequestParam String fromMember,
                            @RequestParam String toMember) {
        followService.unFollow(fromMember, toMember);
    }
}
