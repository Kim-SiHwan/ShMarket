package kim.sihwan.daangn.service.push;

import com.google.firebase.messaging.*;
import kim.sihwan.daangn.domain.member.Keyword;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.MemberKeyword;
import kim.sihwan.daangn.dto.keyword.KeywordListResponseDto;
import kim.sihwan.daangn.dto.keyword.KeywordRequestDto;
import kim.sihwan.daangn.dto.push.NotificationResponse;
import kim.sihwan.daangn.repository.member.KeywordRepository;
import kim.sihwan.daangn.repository.member.MemberKeywordRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PushService {

    private final RedisTemplate<String, String> redisTemplate;
    private final KeywordRepository keywordRepository;
    private final MemberRepository memberRepository;
    private final MemberKeywordRepository memberKeywordRepository;

    public List<KeywordListResponseDto> findAllKeywordsByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberKeywordRepository.findAllByMemberUsername(username).stream()
                .map(KeywordListResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public void addKeyword(KeywordRequestDto keywordRequestDto) throws FirebaseMessagingException {

        Member member = memberRepository.findMemberByNickname(keywordRequestDto.getNickname()).orElseThrow(NoSuchElementException::new);
        Keyword keyword = Keyword.builder()
                .keyword(keywordRequestDto.getKeyword())
                .build();
        keywordRepository.save(keyword);
        MemberKeyword memberKeyword = new MemberKeyword();
        memberKeyword.addMember(member);
        memberKeyword.addKeyword(keyword);
        memberKeywordRepository.save(memberKeyword);
        setTopic(member, keywordRequestDto.getKeyword());

    }

    public void setTopic(Member member, String topic) throws FirebaseMessagingException {

        String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String fcmToken = valueOperations.get(member.getId() + "::FCM");
        List<String> rt = Collections.singletonList(fcmToken);
        FirebaseMessaging.getInstance()
                .subscribeToTopic(rt, encodedTopic);

    }

    public void sendTopic(NotificationResponse notificationResponse) {
        String encodedTopic = URLEncoder.encode(notificationResponse.getTopic(), StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put("time_to_live", "300");
        headers.put("Urgency", "high");

        String sendTitle = notificationResponse.getTopic() + " 키워드 알림이 도착했어요!";
        String sendMessage = notificationResponse.getTopic() + " 키워드에 해당하는 물품이 등록되었어요!";
        System.out.println(sendTitle + " " + sendMessage);
        WebpushNotification webpushNotification = WebpushNotification.builder()
                .setTitle(sendTitle)
                .setBody(sendMessage)
                .setTag(notificationResponse.getProductId().toString())
                .build();

        WebpushConfig webpushConfig = WebpushConfig.builder()
                .putAllHeaders(headers)
                .setNotification(webpushNotification)
                .build();

        Message message = Message.builder()
                .setTopic(encodedTopic)
                .setWebpushConfig(webpushConfig)
                .build();
        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
