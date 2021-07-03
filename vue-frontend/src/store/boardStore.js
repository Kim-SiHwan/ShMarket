import board_api from "@/apis/board_api";
import router from '@/routes/index';

const boardStore = {
    state    : {
        boardList         : [],
        myBoardList       : [],
        boardDetail       : '',
        boardTotalPage    : 0,
        boardCurrentPage  : 1,
        myBoardCurrentPage: 1,
        myBoardTotalPage  : 1
    },
    mutations: {
        SET_BOARD_LIST(state, payload) {
            state.boardList = payload;
        },
        SET_BOARD_DETAIL(state, payload) {
            state.boardDetail = payload
        },
        SET_MY_BOARD_LIST(state, payload) {
            state.myBoardList = payload;
        },
        SET_BOARD_TOTAL_PAGE(state, payload) {
            console.log(payload);
            state.boardTotalPage = payload;
        },
        SET_BOARD_CURRENT_PAGE(state, payload) {
            state.boardCurrentPage = payload;
        },
        SET_MY_BOARD_TOTAL_PAGE(state, payload) {
            state.myBoardTotalPage = payload;
        },
        SET_MY_BOARD_CURRENT_PAGE(state, payload) {
            state.myBoardCurrentPage = payload;
        }
    },
    actions  : {
        async REQUEST_GET_BOARD(context, payload) {
            const response = await board_api.requestGetBoard(payload);
            if (response) {
                context.commit('SET_BOARD_DETAIL', response.data);
            }
        },
        async REQUEST_GET_ALL_BOARDS_PAGES(context, payload) {
            let tempCategory = JSON.parse(payload.category);
            let category = [];
            for (let key in tempCategory) {
                if (tempCategory[key]) {
                    category.push(key);
                }
            }
            let data = {
                page    : payload.page - 1,
                category: category
            }
            const response = await board_api.requestGetAllBoardsPages(data);
            if (response) {
                console.log(response);
                context.commit('SET_BOARD_LIST', response.data.data);
                context.commit('SET_BOARD_TOTAL_PAGE', response.data.totalPage);
            }
        },
        async REQUEST_ADD_BOARD(context, payload) {
            const response = await board_api.requestAddBoard(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '정상적으로 작성되었습니다.', color: 'info'
                });
            }
        },
        async REQUEST_DELETE_BOARD(context, payload) {
            const response = await board_api.requestDeleteBoard(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '정상적으로 삭제되었습니다.', color: 'info'
                });
                await router.push('/boardList');
            }
        },
        async REQUEST_UPDATE_BOARD(context, payload) {
            const response = await board_api.requestUpdateBoard(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '정상적으로 수정되었습니다.', color: 'info'
                });
                context.commit('SET_BOARD_DETAIL', response.data);
            }
        },
        async REQUEST_GET_MY_BOARD(context, payload) {
            const response = await board_api.requestGetMyBoard(payload);
            if (response) {
                context.commit('SET_MY_BOARD_LIST', response.data.data);
                context.commit('SET_MY_BOARD_TOTAL_PAGE', response.data.totalPage);
            }
        }
    }
}

export default boardStore