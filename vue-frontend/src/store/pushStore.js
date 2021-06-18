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
    actions  : {
        async REQUEST_ADD_KEYWORD(context, payload) {
            const response = await push_api.requestAddKeyword(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '키워드를 등록했습니다.', color: 'success'
                });
                await context.dispatch('REQUEST_GET_KEYWORDS', payload.nickname);
            }
        },
        async REQUEST_GET_KEYWORDS(context, payload) {
            const response = await push_api.requestGetKeywords(payload);
            if (response) {
                context.commit('SET_KEYWORD_LIST', response.data);
            }
        },
        async REQUEST_DELETE_KEYWORD(context,payload){
            const response = await push_api.requestDeleteKeyword(payload);
            if(response){
                context.commit('SET_SNACK_BAR',{
                    msg:'삭제되었습니다.', color:'info'
                });
                await context.dispatch('REQUEST_GET_KEYWORDS', payload.nickname);
            }
        }
    }

}

export default pushStore