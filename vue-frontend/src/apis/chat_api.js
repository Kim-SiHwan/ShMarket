import Send from "@/apis/common_api";

function requestAddChatRoom(data) {
    return Send({
        url: '/api/chat',
        method: 'POST',
        data: data
    })
}

function requestGetChatLogs(data) {
    return Send({
        url: '/api/chat/' + data,
        method: 'GET'
    })
}

export default {
    requestAddChatRoom,
    requestGetChatLogs
};