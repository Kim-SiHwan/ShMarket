import Send from "@/apis/common_api";

function requestAddFollow(data) {
    return Send({
        url   : '/api/follow',
        method: 'POST',
        params: {
            fromMember: data.fromMember,
            toMember  : data.toMember
        }
    })
}

function requestGetFollowingsProduct(data) {
    return Send({
        url   : '/api/follow',
        method: 'GET',
        params: {
            nickname: data
        }
    })
}

function requestCancelFollow(data) {
    return Send({
        url   : '/api/follow',
        method: 'DELETE',
        params: {
            fromMember: data.fromMember,
            toMember  : data.toMember
        }
    })
}

function requestGetFollowings(data) {
    return Send({
        url   : '/api/follow/following',
        method: 'GET',
        params: {nickname: data}
    })
}

export default {
    requestAddFollow,
    requestCancelFollow,
    requestGetFollowingsProduct,
    requestGetFollowings
};