import follow_api from "@/apis/follow_api";

const followStore = {
    state    : {
        followings: []
    },
    mutations: {
        SET_FOLLOWINGS(state, payload) {
            state.followings = payload;
        }
    },
    actions  : {
        async REQUEST_ADD_FOLLOW(context, payload) {
            const response = await follow_api.requestAddFollow(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '모아보기에 ' + payload.toMember + '님을 추가했습니다.', color: 'info'
                })
                await context.dispatch('REQUEST_GET_FOLLOWINGS_PRODUCT', payload.toMember);
            }
        },
        async REQUEST_GET_FOLLOWINGS_PRODUCT(context, payload) {
            const response = await follow_api.requestGetFollowingsProduct(payload);
            if (response) {
                context.commit('SET_PRODUCT_LIST', response.data);
            }
        },
        async REQUEST_CANCEL_FOLLOW(context, payload) {
            const response = await follow_api.requestCancelFollow(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.toMember + '님을 모아보기에서 제거했습니다.', color: 'info'
                })
                await context.dispatch('REQUEST_GET_FOLLOWINGS_PRODUCT', payload.toMember);
            }
        },
        async REQUEST_GET_FOLLOWINGS(context, payload) {
            const response = await follow_api.requestGetFollowings(payload);
            if (response) {
                context.commit('SET_FOLLOWINGS', response.data);
            }
        }
    }
}

export default followStore