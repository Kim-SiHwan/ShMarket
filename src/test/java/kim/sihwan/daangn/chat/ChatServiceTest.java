package kim.sihwan.daangn.chat;


import kim.sihwan.daangn.domain.chat.ChatLog;
import kim.sihwan.daangn.domain.chat.ChatRoom;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.dto.chat.ChatRequestDto;
import kim.sihwan.daangn.dto.chat.ChatRoomListResponseDto;
import kim.sihwan.daangn.dto.chat.ChatRoomResponseDto;
import kim.sihwan.daangn.repository.chat.ChatLogRepository;
import kim.sihwan.daangn.repository.chat.ChatRoomRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import kim.sihwan.daangn.repository.product.ProductRepository;
import kim.sihwan.daangn.service.chat.ChatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ChatServiceTest {

    @InjectMocks
    ChatService chatService;

    @Mock
    ChatLogRepository chatLogRepository;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductRepository productRepository;


    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    }


    @Test
    @DisplayName("ChatService.addChatRoom 채팅방 추가 테스트")
    public void 채팅방_생성테스트() throws Exception {

        //given
        ChatRequestDto chatRequestDto = new ChatRequestDto();
        chatRequestDto.setProductId(1L);
        chatRequestDto.setSender("user");
        chatRequestDto.setReceiver("guest");
        chatRequestDto.setMessage("message");

        Product product = new Product();
        given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));

        //when
        chatService.addChatRoom(chatRequestDto);

        //then
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    @DisplayName("ChatService.findAllChatRoomByNickname 닉네임을 통해 사용자의 채팅방리스트 조회 ")
    public void 채팅방전체_조회테스트() throws Exception {

        //given
        ChatRoom chatRoom = ChatRoom.builder()
                .sender("user")
                .receiver("guest")
                .build();

        for (int i = 0; i < 5; i++) {
            ChatLog chatLog = ChatLog.builder()
                    .sender("user")
                    .receiver("guest")
                    .message("msg")
                    .build();

            chatLog.addChatRoom(chatRoom);
        }

        Product product = Product.builder()
                .id(1L)
                .build();

        chatRoom.addProduct(product);


        List<ChatRoom> chatRoomList = new ArrayList<>();
        chatRoomList.add(chatRoom);

        Member member = Member.builder()
                .username("user")
                .nickname("user")
                .build();

        given(chatRoomRepository.findAllBySenderOrReceiver(anyString(), anyString())).willReturn(chatRoomList);
        given(memberRepository.findMemberByUsername(anyString())).willReturn(member);

        //when
        List<ChatRoomListResponseDto> list = chatService.findAllChatRoomByNickname("user");

        //then
        assertEquals(1, list.size());
        assertEquals("user", list.get(0).getSender());

        verify(chatRoomRepository, times(1)).findAllBySenderOrReceiver("user", "user");
        verify(memberRepository, times(1)).findMemberByUsername("user");
    }

    @Test
    @DisplayName("ChatService.findAllChatLogByRoomId 채팅방 채팅 내역 조회 테스트")
    public void 채팅내역_조회테스트() throws Exception {

        //given
        ChatRoom chatRoom = ChatRoom.builder()
                .sender("user")
                .receiver("guest")
                .build();

        for (int i = 0; i < 5; i++) {
            ChatLog chatLog = ChatLog.builder()
                    .sender("user")
                    .receiver("guest")
                    .message("msg" + i)
                    .build();

            chatLog.addChatRoom(chatRoom);
        }

        Product product = Product.builder()
                .id(1L)
                .build();

        chatRoom.addProduct(product);

        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.of(chatRoom));

        //when
        ChatRoomResponseDto responseDto = chatService.findAllChatLogByRoomId(1L, "user");

        //then
        assertEquals(5, responseDto.getChatLogs().size());
        assertEquals("msg4", responseDto.getChatLogs().get(4).getMessage());
        assertEquals(1L, responseDto.getProductId());

        verify(chatRoomRepository, times(1)).findById(anyLong());
    }
}
