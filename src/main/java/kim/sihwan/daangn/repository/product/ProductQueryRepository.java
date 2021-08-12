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

import static kim.sihwan.daangn.domain.board.QBoard.board;
import static kim.sihwan.daangn.domain.product.QProduct.product;

@Slf4j
@Repository
public class ProductQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ProductQueryRepository(JPAQueryFactory queryFactory) {
        super(Product.class);
        this.queryFactory = queryFactory;
    }

    public List<Product> findProducts(int page, int size, String nickname, List<String> blockList, List<String> areaList, List<String> categoryList) {
        List<Long> ids = queryFactory
                .select(product.id)
                .from(product)
                .where(eqNickname(nickname),
                        inBlockList(blockList),
                        inAreaList(areaList),
                        inCategoryList(categoryList))
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

    BooleanExpression inBlockList(List<String> blockList){
        if(blockList == null){
            return null;
        }
        return board.nickname.notIn(blockList);
    }

    BooleanExpression inAreaList(List<String> areaList){
        if(areaList== null){
            return null;
        }
        return board.area.in(areaList);
    }

    BooleanExpression inCategoryList(List<String> categoryList){
        if(categoryList==null){
            return null;
        }
        return board.category.in(categoryList);
    }

}
