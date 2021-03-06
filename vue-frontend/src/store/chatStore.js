import chat_api from "@/apis/chat_api";
import router from "@/routes";

const chatStore = {
    state    : {
        chatLog       : [],
        chatRooms     : [],
        chatRoomDetail: '',
        chatCount     : ''
    },
    mutations: {
        SET_CHAT_ROOMS(state, payload) {
            state.chatRooms = payload;
        },
        SET_CHAT_COUNT(state, payload) {
            state.chatCount = payload;
        },
        SET_CHAT_ROOM_DETAIL(state, payload) {
            state.chatRoomDetail = payload;
        }
    },
    actions  : {
        async REQUEST_ADD_CHATROOM(context, payload) {
            const response = await chat_api.requestAddChatRoom(payload);
            if (response) {
                await router.push({
                    path : '/chat',
                    query: {
                        roomId   : response.data,
                        sender   : payload.sender,
                        receiver : payload.receiver,
                        productId: payload.productId
                    }
                })
            }
        },
        async REQUEST_GET_CHAT_LOGS(context, payload) {
            const response = await chat_api.requestGetChatLogs(payload);
            if (response) {
                context.commit('SET_CHAT_ROOM_DETAIL', response.data);
            }
        },
        async REQUEST_GET_CHAT_ROOMS(context, payload) {
            const response = await chat_api.requestGetChatRooms(payload);
            if (response) {
                context.commit('SET_CHAT_ROOMS', response.data);
            }
        },
        async REQUEST_GET_CHAT_COUNT(context, payload) {
            const response = await chat_api.requestGetNotReadCount(payload);
            if (response) {
                context.commit('SET_CHAT_COUNT', response.data);
            }
        }
    }
}

export default chatStore