package kim.sihwan.daangn.repository.chat;

import kim.sihwan.daangn.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    List<ChatRoom> findAllBySenderOrReceiver(String sender, String receiver);
}
