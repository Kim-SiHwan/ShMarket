package kim.sihwan.daangn.service.push;

import com.google.firebase.messaging.*;
import kim.sihwan.daangn.domain.member.Keyword;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.member.MemberKeyword;
import kim.sihwan.daangn.domain.member.Notice;
import kim.sihwan.daangn.dto.chat.ChatRequestDto;
import kim.sihwan.daangn.dto.keyword.KeywordListResponseDto;
import kim.sihwan.daangn.dto.keyword.KeywordRequestDto;
import kim.sihwan.daangn.dto.push.NotificationResponse;
import kim.sihwan.daangn.repository.member.KeywordRepository;
import kim.sihwan.daangn.repository.member.MemberKeywordRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.service.member.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final NoticeService noticeService;

    public void sendByToken(ChatRequestDto chatRequestDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Member member = memberRepository.findMemberByNickname(chatRequestDto.getReceiver())
                .orElseThrow(NoSuchElementException::new);
        String fcmToken = valueOperations.get(member.getId() + "::FCM");

        WebpushNotification webpushNotification = WebpushNotification.builder()
                .setTitle(chatRequestDto.getSender() + "님으로부터 메시지가 도착했습니다.")
                .setBody(chatRequestDto.getMessage())
                .setTag(chatRequestDto.getRoomId().toString())
                .build();

        WebpushConfig webpushConfig = WebpushConfig.builder()
                .setNotification(webpushNotification)
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setWebpushConfig(webpushConfig)
                .putData("sender", chatRequestDto.getSender())
                .putData("receiver", chatRequestDto.getReceiver())
                .putData("productId", chatRequestDto.getProductId().toString())
                .putData("roomId", chatRequestDto.getRoomId().toString())
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);

        Notice notice = Notice.builder()
                .type("채팅 알림")
                .target(chatRequestDto.getRoomId())
                .message(chatRequestDto.getSender()+"님에게 메시지가 도착했습니다.")
                .build();
        noticeService.addNotice(notice,chatRequestDto.getReceiver());
    }

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

    @Transactional
    public void sendTopic(NotificationResponse notificationResponse) {
        String encodedTopic = URLEncoder.encode(notificationResponse.getTopic(), StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put("time_to_live", "300");
        headers.put("Urgency", "high");

        String sendTitle = notificationResponse.getTopic() + " 키워드 알림이 도착했어요!";
        String sendMessage = notificationResponse.getTopic() + " 키워드에 해당하는 물품이 등록되었어요!";
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
                .putData("productId", notificationResponse.getProductId().toString())
                .build();
        FirebaseMessaging.getInstance().sendAsync(message);

        List<String> receivers = getReceiverByKeyword(notificationResponse.getTopic());
        receivers.forEach(r->{
            Notice notice = Notice.builder()
                    .type("키워드 알림")
                    .target(notificationResponse.getProductId())
                    .message(notificationResponse.getTopic()+"키워드 알림이 도착했습니다.")
                    .build();
            noticeService.addNotice(notice,r);
        });


    }

    @Transactional
    public List<String> getReceiverByKeyword(String keyword){
        List<MemberKeyword> list = memberKeywordRepository.findAllByKeywordKeyword(keyword);
        return list.stream()
                .map(m->m.getMember().getUsername())
                .collect(Collectors.toList());
    }
}
