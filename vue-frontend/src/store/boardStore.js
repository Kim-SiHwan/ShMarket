import board_api from "@/apis/board_api";
import router from '@/routes/index';

const boardStore = {
    state    : {
        boardList  : [],
        boardDetail: '',
        hasNext:false,
    },
    mutations: {
        SET_BOARD_LIST(state, payload) {
            state.boardList = payload;
        },
        SET_BOARD_DETAIL(state, payload) {
            state.boardDetail = payload
        },
        SET_HAS_NEXT(state, payload){
            state.hasNext = payload;
        }
    },
    actions  : {
        async REQUEST_GET_BOARD(context, payload) {
            const response = await board_api.requestGetBoard(payload);
            if (response) {
                context.commit('SET_BOARD_DETAIL', response.data);
            }
        },
        async REQUEST_GET_ALL_PAGES(context, payload) {
            let tempCategory = JSON.parse(payload.category);
            let category = [];
            for (let key in tempCategory) {
                if (tempCategory[key]) {
                    category.push(key);
                }
            }
            let data = {
                page : payload.page,
                category : category
            }
            const response = await board_api.requestGetAllPages(data);
            if (response) {
                context.commit('SET_BOARD_LIST', response.data);
                context.commit('SET_HAS_NEXT',response.data.next);
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
                context.commit('SET_BOARD_LIST', response.data);
            }
        }
    }
}

export default boardStore