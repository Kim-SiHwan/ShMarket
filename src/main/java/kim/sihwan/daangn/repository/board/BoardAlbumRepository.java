package kim.sihwan.daangn.repository.board;

import io.lettuce.core.dynamic.annotation.Param;
import kim.sihwan.daangn.domain.board.BoardAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardAlbumRepository extends JpaRepository<BoardAlbum,Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM BoardAlbum b WHERE b.id IN :ids")
    void deleteAllByBoardAlbumIds(@Param("ids")List<Long> ids);
}
