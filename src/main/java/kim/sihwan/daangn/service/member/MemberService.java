package kim.sihwan.daangn.service.member;


import kim.sihwan.daangn.config.jwt.JwtTokenProvider;
import kim.sihwan.daangn.domain.area.Area;
import kim.sihwan.daangn.domain.member.Block;
import kim.sihwan.daangn.domain.member.Manner;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.Review;
import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.dto.member.MemberResponseDto;
import kim.sihwan.daangn.dto.member.block.BlockDto;
import kim.sihwan.daangn.dto.member.manner.MannerDto;
import kim.sihwan.daangn.dto.member.review.ReviewDto;
import kim.sihwan.daangn.exception.customException.*;
import kim.sihwan.daangn.repository.area.AreaRepository;
import kim.sihwan.daangn.repository.member.BlockRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final AreaRepository areaRepository;
    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;

    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, User> userRedisTemplate;

    public MemberResponseDto findById(Long memberId) {
        return new MemberResponseDto(memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new));
    }

    @Transactional
    public void addManner(MannerDto mannerRequestDto) {
        Member member = memberRepository.findMemberByNickname(mannerRequestDto.getNickname())
                .orElseThrow(NoSuchElementException::new);
        Manner manner = mannerRequestDto.toEntity(mannerRequestDto);
        manner.addMember(member);
    }

    @Transactional
    public void addReview(ReviewDto reviewDto) {
        Member member = memberRepository.findMemberByNickname(reviewDto.getNickname())
                .orElseThrow(NoSuchElementException::new);
        Review review = reviewDto.toEntity(reviewDto);
        review.addMember(member);
    }

    public List<MannerDto> getMannersByNickname(String nickname) {
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(NoSuchElementException::new);
        return member.getManners().stream()
                .map(MannerDto::toDto)
                .sorted(Comparator.comparing(MannerDto::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByNickname(String nickname) {
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(NoSuchElementException::new);
        return member.getReviews().stream()
                .map(ReviewDto::toDto)
                .sorted(Comparator.comparing(ReviewDto::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addBlock(BlockDto blockDto) {
        Member fromMember = memberRepository.findMemberByNickname(blockDto.getFromNickname()).orElseThrow(NoSuchElementException::new);
        Optional<Block> getBlock = blockRepository.findByMemberNicknameAndToMember(blockDto.getFromNickname(), blockDto.getToNickname());

        if (getBlock.isPresent()) {
            throw new AlreadyExistException("block");
        }

        if (fromMember.getBlocks().size() >= 5) {
            throw new OverSizeException("block");
        }

        Block block = blockDto.toEntity(blockDto);
        block.addMember(fromMember);
    }

    public List<BlockDto> getBlocksByNickname(String nickname) {
        Member member = memberRepository.findMemberByNickname(nickname).orElseThrow(NoSuchElementException::new);
        return member.getBlocks().stream()
                .map(BlockDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBlock(Long blockId) {
        blockRepository.deleteById(blockId);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        ValueOperations<String, User> vo = userRedisTemplate.opsForValue();

        User getUser = vo.get(username + "::MEMBER");
        if (vo.get(username + "::MEMBER") == null) {
            Member member = memberRepository.findMemberByUsername(username);
            if (member == null)
                throw new UserNotFoundException();
            vo.set(username + "::MEMBER", new User(member.getUsername(), member.getPassword(), Collections.singleton(new SimpleGrantedAuthority(member.getRole()))), 30L, TimeUnit.MINUTES);
            return new User(member.getUsername(), member.getPassword(), Collections.singleton(new SimpleGrantedAuthority(member.getRole())));
        }
        return getUser;
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Member member = memberRepository.findMemberByUsername(loginRequestDto.getUsername());
        if (member == null) {
            throw new UserNotFoundException();
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        String jwt = tokenProvider.createToken(member.getUsername());
        if (loginRequestDto.getFcmToken() != null) {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(member.getId() + "::FCM", loginRequestDto.getFcmToken());
        }

        return new LoginResponseDto(member.getId(), jwt, member.getUsername(), member.getNickname(), member.getArea().split(" ")[2]);
    }

    private Boolean isValidateDuplicateUsername(Member member) {
        Member getMember = memberRepository.findMemberByUsername(member.getUsername());
        return getMember == null;
    }

    private Boolean isValidateDuplicateNickname(Member member) {
        Optional<Member> getMember = memberRepository.findMemberByNickname(member.getNickname());
        return getMember.isEmpty();
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto) {
        Member member = joinRequestDto.toEntity(joinRequestDto, passwordEncoder);
        if (!isValidateDuplicateUsername(member)) {
            throw new UsernameDuplicatedException();
        }
        if (!isValidateDuplicateNickname(member)) {
            throw new NicknameDuplicatedException();
        }
        memberRepository.save(member);
        setNearArea(joinRequestDto.getArea());
        return member.getId();
    }

    public ArrayList<String> setNearArea(String area) {

        String[] splitStr = area.split(" ");
        String city = splitStr[0];
        List<Area> list = areaRepository.findAllByCityLike("%" + city + "%");

        Area getArea = areaRepository.findByAddress(area);

        ArrayList<String> nearArea = new ArrayList<>();
        list.forEach(l -> {
            double distanceMeter =
                    distance(Double.parseDouble(getArea.getLat()), Double.parseDouble(getArea.getLng()), Double.parseDouble(l.getLat()), Double.parseDouble(l.getLng()), "meter");
            if (distanceMeter <= 3000) {
                nearArea.add(l.getDong());
            }
        });
        ListOperations<String, String> vo = redisTemplate.opsForList();
        if (vo.range(getArea.getAddress() + "::List", 0L, -1L).isEmpty()) {
            vo.leftPushAll(getArea.getAddress() + "::List", nearArea);
        }

        return nearArea;
    }

    public double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit.equals("kilometer")) {
            dist = dist * 1.609344;
        } else if (unit.equals("meter")) {
            dist = dist * 1609.344;
        }

        return (dist);
    }


    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}
