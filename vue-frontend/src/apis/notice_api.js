import Send from "@/apis/common_api";

function requestGetNotices(data) {
    return Send({
        url: '/api/notice',
        method: 'GET',
        params:{nickname:data}
    })
}

function requestReadNotice(data) {
    return Send({
        url: '/api/notice/' + data,
        method: 'PATCH'
    })
}

function requestReadAllNotice(data){
    return Send({
        url:'/api/notice',
        method:'PATCH',
        params:{nickname:data}
    })
}
export default {
    requestGetNotices,
    requestReadNotice,
    requestReadAllNotice
};