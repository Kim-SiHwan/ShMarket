import Send from "@/apis/common_api";

function requestJoin(member) {
    return Send({
        url   : '/api/member/join',
        method: 'POST',
        data  : member
    });
}

function requestLogin(member) {
    return Send({
        url   : '/api/member/login',
        method: 'POST',
        data  : member
    });
}

function requestLogout() {
    return Send({
        url   : '/api/member/logout',
        method: 'POST',
    });
}

function requestAddManner(data) {
    return Send({
        url   : '/api/member/manner',
        method: 'POST',
        data  : data
    });
}

function requestGetManners(data) {
    return Send({
        url   : '/api/member/manner',
        method: 'GET',
        params: {nickname: data}
    });
}

function requestAddReview(data) {
    return Send({
        url   : '/api/member/review',
        method: 'POST',
        data  : data
    });
}

function requestGetReviews(data) {
    return Send({
        url   : '/api/member/review',
        method: 'GET',
        params: {nickname: data}
    });
}

function requestGetNotices(data) {
    return Send({
        url   : '/api/notice',
        method: 'GET',
        params: {nickname: data}
    });
}

function requestAddBlock(data) {
    return Send({
        url   : '/api/member/block',
        method: 'POST',
        data  : data
    });
}

function requestGetBlocks(data) {
    return Send({
        url   : '/api/member/block',
        method: 'GET',
        params: {nickname: data}
    });
}

function requestDeleteBlock(data) {
    return Send({
        url   : '/api/member/block/' + data,
        method: 'DELETE',
    });
}

function requestSendEmail(data) {
    return Send({
        url   : '/api/member/email',
        method: 'GET',
        params: {email: data}
    });
}

export default {
    requestJoin,
    requestLogin,
    requestLogout,
    requestAddManner,
    requestGetManners,
    requestAddReview,
    requestGetReviews,
    requestGetNotices,
    requestAddBlock,
    requestGetBlocks,
    requestDeleteBlock,
    requestSendEmail
};