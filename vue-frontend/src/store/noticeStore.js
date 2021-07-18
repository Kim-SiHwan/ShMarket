import notice_api from "@/apis/notice_api";

const noticeStore = {
    state    : {
        notices    : [],
        noticeCount: ''
    },
    mutations: {
        SET_NOTICES(state, payload) {
            state.notices = payload;
        },
        SET_NOTICE_COUNT(state, payload) {
            state.noticeCount = payload;
        }
    },
    actions  : {
        async REQUEST_GET_NOTICES(context, payload) {
            const response = await notice_api.requestGetNotices(payload);
            if (response) {
                context.commit('SET_NOTICES', response.data);
            }
        },
        async REQUEST_READ_NOTICE(context, payload) {
            await notice_api.requestReadNotice(payload.noticeId);
        },
        async REQUEST_READ_ALL_NOTICE(context, payload) {
            await notice_api.requestReadAllNotice(payload);
        },
        async REQUEST_GET_NOTICE_COUNT(context, payload) {
            const response = await notice_api.requestGetNotReadNotice(payload);
            if (response) {
                context.commit('SET_NOTICE_COUNT', response.data);
            }
        }
    }
}

export default noticeStore