package kim.sihwan.daangn.service.member;

import kim.sihwan.daangn.domain.member.Follow;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.dto.product.ProductListResponseDto;
import kim.sihwan.daangn.exception.customException.AlreadyExistException;
import kim.sihwan.daangn.exception.customException.OverSizeException;
import kim.sihwan.daangn.repository.member.FollowRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    private final ProductService productService;

    @Transactional
    public void addFollow(String fromNickname, String toNickname) {
        Member fromMember = memberRepository.findMemberByNickname(fromNickname).orElseThrow(NoSuchElementException::new);
        Member toMember = memberRepository.findMemberByNickname(toNickname).orElseThrow(NoSuchElementException::new);

        if(followRepository.findByFromMemberNicknameAndToMemberNickname(fromNickname, toNickname)!=null){
            throw new AlreadyExistException("follow");
        }

        if(followRepository.findAllByFromMemberNickname(fromNickname).size()>5){
            throw new OverSizeException("follow");
        }
        Follow follow = new Follow();
        follow.addFromMember(fromMember);
        follow.addToMember(toMember);
        followRepository.save(follow);
    }

    @Transactional
    public void unFollow(String fromNickname, String toNickname) {
        Follow follow = followRepository.findByFromMemberNicknameAndToMemberNickname(fromNickname, toNickname);
        followRepository.delete(follow);
    }

    public List<String> getFollowings(String nickname) {
        List<Follow> list = followRepository.findAllByFromMemberNickname(nickname);
        return list.stream()
                .map(m -> m.getToMember().getNickname())
                .collect(Collectors.toList());
    }

    public List<ProductListResponseDto> getProductsOfFollowings(String nickname) {
        List<ProductListResponseDto> result = new ArrayList<>();
        getFollowings(nickname).forEach(s -> result.addAll(productService.findAllMyProduct(nickname, s)));
        return result;
    }
}
