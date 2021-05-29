package kim.sihwan.daangn.repository.chat;

import kim.sihwan.daangn.domain.chat.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog,Long> {

    List<ChatLog> findAllByChatRoomId(Long id);

}
