import Send from "@/apis/common_api";

function requestAddBoard(data) {
    return Send({
        url    : '/api/board',
        method : 'POST',
        headers: {'Content-Type': 'multipart/form-data'},
        data   : data
    })
}

function requestGetAllBoards(data) {
    return Send({
        url   : '/api/board',
        method: 'GET',
        params: {
            'list': encodeURI(data)
        }
    })
}

function requestGetAllBoardsPages(data) {
    return Send({
        url   : '/api/board/list/' + data.page,
        method: 'GET',
        params: {
            'list': encodeURI(data.category)
        }
    })
}

function requestGetBoard(data) {
    return Send({
        url   : '/api/board/' + data,
        method: 'GET'
    })
}

function requestDeleteBoard(data) {
    return Send({
        url   : '/api/board/' + data,
        method: 'DELETE'
    })
}

function requestUpdateBoard(data) {
    return Send({
        url    : '/api/board',
        method : 'PUT',
        headers: {'Content-Type': 'multipart/form-data'},
        data   : data
    })
}

function requestGetMyBoard(data) {
    return Send({
        url   : '/api/board/my/' + data.page,
        method: 'GET',
        params: {nickname: data.nickname}
    })
}

export default {
    requestAddBoard,
    requestGetAllBoards,
    requestGetBoard,
    requestDeleteBoard,
    requestUpdateBoard,
    requestGetMyBoard,
    requestGetAllBoardsPages
};