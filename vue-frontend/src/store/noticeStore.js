import notice_api from "@/apis/notice_api";

const noticeStore = {
    state: {
        notices:[]
    },
    mutations: {
        SET_NOTICES(state, payload) {
            state.notices = payload;
        }
    },
    actions: {
        async REQUEST_GET_NOTICES(context, payload) {
            console.log(payload);
            const response = await notice_api.requestGetNotices(payload);
            if (response) {
                context.commit('SET_NOTICES', response.data);
            }
        },
        async REQUEST_READ_NOTICE(context, payload){
            await notice_api.requestReadNotice(payload);
        },
        async REQUEST_READ_ALL_NOTICE(context, payload){
            await notice_api.requestReadAllNotice(payload);
        }
    }
}

export default noticeStore