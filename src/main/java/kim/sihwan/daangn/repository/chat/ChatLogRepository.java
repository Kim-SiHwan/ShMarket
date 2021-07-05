package kim.sihwan.daangn.repository.chat;

import kim.sihwan.daangn.domain.chat.ChatLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    @EntityGraph(attributePaths = {"chatRoom"})
    List<ChatLog> findAllByChatRoomIdAndCheckedAndReceiver(Long id, boolean isRead, String nickname);

    int countAllByChatRoomIdAndCheckedAndReceiver(Long id, boolean isRead, String nickname);

    int countAllByReceiverAndChecked(String nickname, boolean isRead);


}
