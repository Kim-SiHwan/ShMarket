package kim.sihwan.daangn.repository.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kim.sihwan.daangn.domain.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static kim.sihwan.daangn.domain.product.QProduct.product;

@Slf4j
@Repository
public class ProductQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ProductQueryRepository(JPAQueryFactory queryFactory) {
        super(Product.class);
        this.queryFactory = queryFactory;
    }

    public List<Product> findProducts(int page, int size, String nickname) {
        List<Long> ids = queryFactory
                .select(product.id)
                .from(product)
                .where(eqNickname(nickname))
                .orderBy(product.id.desc())
                .limit(size)
                .offset(page * size)
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return queryFactory
                .selectFrom(product)
                .where(product.id.in(ids))
                .orderBy(product.id.desc())
                .limit(size)
                .fetch();
    }

    BooleanExpression eqNickname(String nickname) {
        if (nickname == null) {
            return null;
        }
        return product.nickname.like(nickname + "%");
    }

}
