package kim.sihwan.daangn.service.member;


import kim.sihwan.daangn.config.jwt.JwtTokenProvider;
import kim.sihwan.daangn.domain.area.Area;
import kim.sihwan.daangn.domain.member.Manner;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.area.SelectedArea;
import kim.sihwan.daangn.domain.member.Notice;
import kim.sihwan.daangn.domain.member.Review;
import kim.sihwan.daangn.dto.member.JoinRequestDto;
import kim.sihwan.daangn.dto.member.LoginRequestDto;
import kim.sihwan.daangn.dto.member.LoginResponseDto;
import kim.sihwan.daangn.dto.member.MemberResponseDto;

import kim.sihwan.daangn.dto.member.manner.MannerDto;
import kim.sihwan.daangn.dto.member.notice.NoticeResponseDto;
import kim.sihwan.daangn.dto.member.review.ReviewDto;
import kim.sihwan.daangn.exception.customException.UsernameDuplicatedException;
import kim.sihwan.daangn.repository.area.AreaRepository;
import kim.sihwan.daangn.repository.area.SelectedAreaRepository;
import kim.sihwan.daangn.repository.member.MannerRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.member.NoticeRepository;
import kim.sihwan.daangn.repository.member.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    private final MemberRepository memberRepository;
    private final AreaRepository areaRepository;
    private final SelectedAreaRepository selectedAreaRepository;
    private final MannerRepository mannerRepository;
    private final ReviewRepository reviewRepository;
    private final NoticeRepository noticeRepository;

    @Transactional
    public void addManner(MannerDto mannerRequestDto){
        Member member = memberRepository.findMemberByNickname(mannerRequestDto.getNickname())
                .orElseThrow(NoSuchElementException::new);
        Manner manner = mannerRequestDto.toEntity(mannerRequestDto);
        manner.addMember(member);
    }

    @Transactional
    public void addReview(ReviewDto reviewDto){
        Member member = memberRepository.findMemberByNickname(reviewDto.getNickname())
                .orElseThrow(NoSuchElementException::new);
        Review review = reviewDto.toEntity(reviewDto);
        review.addMember(member);
    }

    public List<MannerDto> getMannersByNickname(String nickname){
        return mannerRepository.findAllByMemberNickname(nickname).stream()
                .map(MannerDto::toDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByNickname(String nickname){
        return reviewRepository.findAllByMemberNickname(nickname).stream()
                .map(ReviewDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addNotice(Notice notice,String nickname){
        Member member = memberRepository.findMemberByNickname(nickname)
                .orElseThrow(NoSuchElementException::new);
        notice.addMember(member);
        noticeRepository.save(notice);

    }

    public List<NoticeResponseDto> getNoticesByNickname(String nickname){
        return noticeRepository.findAllByMemberNickname(nickname).stream()
                .map(NoticeResponseDto::toDto)
                .collect(Collectors.toList());
    }

    private Boolean isValidateDuplicateMember(Member member) {
        Member getMember = memberRepository.findMemberByUsername(member.getUsername());
        if(getMember ==null){
            return true;
        }
        return false;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        Authentication auth = managerBuilder.getObject().authenticate(token);

        Member member = memberRepository.findMemberByUsername(auth.getName());

        String jwt = tokenProvider.createToken(auth);

        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(member.getId()+"::FCM", loginRequestDto.getFcmToken());

        return new LoginResponseDto(member.getId(),jwt, member.getUsername(), member.getNickname(), member.getArea().split(" ")[2]);
    }

    @Transactional
    public Long join(JoinRequestDto joinRequestDto){
        Member member = joinRequestDto.toEntity(joinRequestDto,passwordEncoder);
        System.out.println(member.getUsername());
        if (!isValidateDuplicateMember(member)) {
            throw new UsernameDuplicatedException();
        }
        memberRepository.save(member);

        Area area = areaRepository.findByAddress(joinRequestDto.getArea());
        SelectedArea selectedArea = new SelectedArea();
        selectedArea.addMember(member);
        selectedArea.addArea(area);
        selectedAreaRepository.save(selectedArea);

        String[] splitStr= joinRequestDto.getArea().split(" ");
        String city = splitStr[0];
        List<Area> list = areaRepository.findAllByCityLike("%"+city+"%");

        Area getArea = areaRepository.findByAddress(joinRequestDto.getArea());

        List<String> nearArea = new ArrayList<>();
        list.forEach(l->{
            double distanceMeter =
                    distance(Double.parseDouble(getArea.getLat()), Double.parseDouble(getArea.getLng()), Double.parseDouble(l.getLat()), Double.parseDouble(l.getLng()), "meter");
            if(distanceMeter<=3000) {
                nearArea.add(l.getDong());
            }
        });
        ListOperations<String,String> vo = redisTemplate.opsForList();
        if(!vo.getOperations().hasKey(getArea.getAddress()+"::List")) {
            vo.leftPushAll(getArea.getAddress() + "::List", nearArea);
        }
        return member.getId();
    }

    public MemberResponseDto findById(Long memberId){
        return new MemberResponseDto(memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new));

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findMemberByUsername(username);
        return new User(member.getUsername(), member.getPassword(), Collections.singleton(new SimpleGrantedAuthority(member.getRole())));
    }


    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit.equals("kilometer")) {
            dist = dist * 1.609344;
        } else if(unit.equals("meter")){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}
