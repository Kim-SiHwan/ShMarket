package kim.sihwan.daangn.repository.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kim.sihwan.daangn.domain.board.Board;
import kim.sihwan.daangn.dto.board.QBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static kim.sihwan.daangn.domain.board.QBoard.board;

@Slf4j
@Repository
public class BoardQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public BoardQueryRepository(JPAQueryFactory queryFactory) {
        super(Board.class);
        this.queryFactory = queryFactory;
    }

    public long getPages(String nickname, List<String> blockList,List<String> areaList, List<String> categoryList){
        return queryFactory
                .selectFrom(board)
                .where(eqNickname(nickname),
                        inBlockList(blockList),
                        inAreaList(areaList),
                        inCategoryList(categoryList))
                .fetchCount();
    }

    public List<QBoardDto> findBoards(int off, int size, String nickname, List<String> blockList,List<String> areaList, List<String> categoryList) {
        List<Long> ids = queryFactory
                .select(board.id)
                .from(board)
                .where(eqNickname(nickname),
                        inBlockList(blockList),
                        inAreaList(areaList),
                        inCategoryList(categoryList))
                .orderBy(board.id.desc())
                .limit(size)
                .offset(off * size)
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(Projections.constructor(QBoardDto.class,
                        board.id,
                        board.area,
                        board.title,
                        board.content,
                        board.nickname,
                        board.updateDate,
                        board.thumbnail,
                        board.category,
                        board.boardAlbums.size(),
                        board.comments.size()
                ))
                .from(board)
                .where(
                        board.id.in(ids)
                )
                .orderBy(board.id.desc())
                .fetch();

    }

    BooleanExpression eqNickname(String nickname) {
        if (nickname == null) {
            return null;
        }
        return board.nickname.like(nickname+"%");
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
