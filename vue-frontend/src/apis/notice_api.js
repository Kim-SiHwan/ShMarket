import Send from "@/apis/common_api";

function requestGetNotices(data) {
    return Send({
        url: '/api/notice',
        method: 'GET',
        params:{nickname:data}
    });
}

function requestReadNotice(data) {
    return Send({
        url: '/api/notice/' + data,
        method: 'PATCH'
    });
}

function requestReadAllNotice(data){
    return Send({
        url:'/api/notice',
        method:'PATCH',
        params:{nickname:data}
    });
}

function requestGetNotReadNotice(data){
    return Send({
        url:'/api/notice/read',
        method:'GET',
        params:{nickname:data}
    });
}

export default {
    requestGetNotices,
    requestReadNotice,
    requestReadAllNotice,
    requestGetNotReadNotice
};