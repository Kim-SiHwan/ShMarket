import Send from "@/apis/common_api";

function requestAddKeyword(data) {
    return Send({
        url: '/api/push/topic',
        method: 'POST',
        data: data
    })

}

function requestGetKeywords() {
    return Send({
        url: '/api/push/keywords',
        method: 'GET'
    })
}

export default {requestAddKeyword, requestGetKeywords}
