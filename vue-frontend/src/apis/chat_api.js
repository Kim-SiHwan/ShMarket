import Send from "@/apis/common_api";

function requestAddChatRoom(data) {
    return Send({
        url: '/api/chat',
        method: 'POST',
        data: data
    });
}

function requestGetChatLogs(data) {
    return Send({
        url: '/api/chat/' + data.roomId,
        method: 'GET',
        params: {nickname: data.nickname}
    });
}

function requestGetChatRooms(data) {
    return Send({
        url: '/api/chat',
        method: 'GET',
        params: {nickname: data}
    });
}

function requestGetNotReadCount(data) {
    return Send({
        url: '/api/chat/read',
        method: 'GET',
        params: {nickname: data}
    });
}

export default {
    requestAddChatRoom,
    requestGetChatLogs,
    requestGetChatRooms,
    requestGetNotReadCount
};