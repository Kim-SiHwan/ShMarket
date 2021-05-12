package kim.sihwan.daangn.domain.product;


import kim.sihwan.daangn.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String username;
    private String nickname;
    private String thumbnail;
    private String category;
    private int read;
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductAlbum> productAlbums = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductTag> productTags = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductInterested> productInteresteds = new HashSet<>();


    @Builder
    public Product(String area, String username,String nickname, String thumbnail, String title, String content, String category) {
        this.area = area;
        this.username = username;
        this.nickname = nickname;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.category = category;
        this.read = 0;
        this.status = ProductStatus.SALE;
        this.createDate = LocalDateTime.now();
    }

    public void setStatusReservation(){
        this.status = ProductStatus.RESERVATION;
    }

    public void setStatusCompleted(){
        this.status = ProductStatus.COMPLETED;
    }

    public void setStatusSale(){
        this.status = ProductStatus.SALE;
    }

    public void addRead(){
        this.read++;
    }

    public void addMember(Member member){
        this.member =member;
    }

    public void addThumbnail(String thumbnailUrl) {
        this.thumbnail = thumbnailUrl;
    }



}
