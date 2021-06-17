import Send from "@/apis/common_api";

function requestGetAllQna() {
    return Send({
        url   : '/api/qna',
        method: 'GET'
    })
}

function requestGetAllMyQna(data) {
    return Send({
        url   : '/api/qna/my',
        method: 'GET',
        params: {nickname: data}
    })
}

function requestGetQna(data) {
    return Send({
        url   : '/api/qna/' + data,
        method: 'GET',
    })
}

function requestAddQna(data) {
    return Send({
        url   : '/api/qna',
        method: 'POST',
        data  : data
    })
}

function requestDeleteQna(data) {
    return Send({
        url   : '/api/qna/' + data,
        method: 'DELETE',
    })
}

export default {
    requestGetAllQna,
    requestGetAllMyQna,
    requestGetQna,
    requestAddQna,
    requestDeleteQna
}