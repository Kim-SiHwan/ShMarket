import Send from "@/apis/common_api";

function requestAddKeyword(data) {
    return Send({
        url: '/api/push/topic',
        method: 'POST',
        data: data
    })

}

function requestGetKeywords(data) {
    return Send({
        url: '/api/push/keywords',
        method: 'GET',
        params:{nickname : data}
    })
}

function requestDeleteKeyword(data){
    return Send({
        url:'/api/push/keyword',
        method:'DELETE',
        data:data
    })
}

export default {
    requestAddKeyword,
    requestGetKeywords,
    requestDeleteKeyword
}
