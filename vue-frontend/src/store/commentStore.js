import comment_api from "@/apis/comment_api";

const commentStore = {
    state    : {
        commentList: []
    },
    mutations: {
        SET_COMMENT_LIST(state, payload) {
            state.commentList = payload;
        }
    },
    actions  : {
        async REQUEST_GET_COMMENTS_BY_BOARD_ID(context, payload) {
            const response = await comment_api.requestGetCommentsByBoardId(payload);
            if (response) {
                context.commit('SET_COMMENT_LIST', response.data);
            }
        },
        async REQUEST_ADD_COMMENT(context, payload) {
            const response = await comment_api.requestAddComment(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '댓글이 등록되었습니다.', color: 'success'
                })
                await context.dispatch('REQUEST_GET_COMMENTS_BY_BOARD_ID', payload.boardId);
            }
        },
        async REQUEST_DELETE_COMMENT(context, payload) {
            const response = await comment_api.requestDeleteCommentByCommentId(payload.commentId);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '댓글이 삭제되었습니다.', color: 'success'
                })
                await context.dispatch('REQUEST_GET_COMMENTS_BY_BOARD_ID', payload.boardId);
            }
        },
        async REQUEST_UPDATE_COMMENT(context, payload) {
            const response = await comment_api.requestUpdateComment(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '댓글이 수정되었습니다.', color: 'success'
                })
                await context.dispatch('REQUEST_GET_COMMENTS_BY_BOARD_ID', payload.boardId);
            }
        }
    }

}

export default commentStore