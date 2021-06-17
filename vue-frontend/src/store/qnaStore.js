import qna_api from "@/apis/qna_api";

const qnaStore = {
    state    : {
        qnaList  : [],
        qnaDetail: ''
    },
    mutations: {
        SET_QNA_LIST(state, payload) {
            state.qnaList = payload;
        },
        SET_QNA_DETAIL(state, payload) {
            state.qnaDetail = payload;
        }
    },
    actions  : {
        async REQUEST_GET_ALL_QNA(context) {
            const response = await qna_api.requestGetAllQna();
            if (response) {
                console.log(response.data);
                context.commit('SET_QNA_LIST', response.data);
            }
        },
        async REQUEST_GET_ALL_MY_QNA(context, payload) {
            const response = await qna_api.requestGetAllMyQna(payload);
            if (response) {
                context.commit('SET_QNA_LIST', response.data);
            }
        },
        async REQUEST_GET_QNA(context, payload) {
            const response = await qna_api.requestGetQna(payload);
            if (response) {
                context.commit('SET_QNA_DETAIL', response.data);
            }
        },
        async REQUEST_ADD_QNA(context, payload) {
            console.log(payload.title + " " + payload.nickname + " " + payload.content);
            const response = await qna_api.requestAddQna(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '문의사항이 등록되었습니다.', color: 'info'
                })
            }
        },
        async REQUEST_DELETE_QNA(context, payload) {
            const response = await qna_api.requestDeleteQna(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '문의사항이 삭제되었습니다.', color: 'info'
                })
            }
        }
    }
}
export default qnaStore