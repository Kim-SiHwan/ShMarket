package kim.sihwan.daangn.repository.product;

import io.lettuce.core.dynamic.annotation.Param;
import kim.sihwan.daangn.domain.product.ProductAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductAlbumRepository extends JpaRepository<ProductAlbum, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ProductAlbum p WHERE p.id IN :ids")
    void deleteAllByProductAlbumIds(@Param("ids") List<Long> ids);
}
