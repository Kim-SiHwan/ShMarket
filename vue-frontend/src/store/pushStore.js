import push_api from "@/apis/push_api";

const pushStore = {
    state: {
        keywordList: []
    },

    mutations: {
        SET_KEYWORD_LIST(state, payload) {
            state.keywordList = payload;
        }
    },
    actions: {
        async REQUEST_ADD_KEYWORD(context, payload) {
            const response = await push_api.requestAddKeyword(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '키워드를 등록했습니다.', color: 'success'
                });
            }
        },
        async REQUEST_GET_KEYWORDS(context) {
            const response = await push_api.requestGetKeywords();
            if (response) {
                context.commit('SET_KEYWORD_LIST', response.data);
            }
        }
    }

}

export default pushStore