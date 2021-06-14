package kim.sihwan.daangn.repository.chat;

import kim.sihwan.daangn.domain.chat.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findAllByChatRoomId(Long id);

    List<ChatLog> findAllByChatRoomIdAndReadAndReceiver(Long id, boolean isRead, String nickname);

    int countAllByChatRoomIdAndReadAndReceiver(Long id, boolean isRead, String nickname);

    int countAllByReceiverAndRead(String nickname, boolean isRead);

    ChatLog findTop1ByChatRoomIdOrderByIdDesc(Long id);



}
