package kim.sihwan.daangn.domain.product;

import kim.sihwan.daangn.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String area;
    private String title;
    private String content;
    private String nickname;
    private Long thumbnailId;
    private String thumbnail;
    private String category;
    private String price;
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductAlbum> productAlbums = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductTag> productTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductLike> productLikes = new LinkedHashSet<>();

    public void setStatusReservation() {
        this.status = ProductStatus.RESERVATION;
    }

    public void setStatusCompleted() {
        this.status = ProductStatus.COMPLETED;
    }

    public void setStatusSale() {
        this.status = ProductStatus.SALE;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addThumbnail(Long thumbnailId, String thumbnailUrl) {
        this.thumbnailId = thumbnailId;
        this.thumbnail = thumbnailUrl;
    }

    public void removeThumbnail() {
        this.thumbnail = "http://localhost:8080/api/product/download?fileName=default.png";
        this.thumbnailId = 0L;
    }

    @Builder
    public Product(Long id, String area, String price, String nickname, String title, String content, String category) {
        this.id = id;
        this.area = area;
        this.nickname = nickname;
        this.price = price;
        this.thumbnail = "http://localhost:8080/api/product/download?fileName=default.png";
        this.title = title;
        this.content = content;
        this.category = category;
        this.status = ProductStatus.SALE;
        this.createDate = LocalDateTime.now();
    }

    public void addMember(Member member) {
        this.member = member;
    }


}
