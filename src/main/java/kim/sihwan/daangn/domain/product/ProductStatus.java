package kim.sihwan.daangn.domain.product;

import lombok.Getter;

@Getter
public enum ProductStatus {
    SALE("판매중"),
    RESERVATION("예약중"),
    COMPLETED("판매 완료");

    private final String description;

    ProductStatus(String description){
        this.description = description;
    }
}
