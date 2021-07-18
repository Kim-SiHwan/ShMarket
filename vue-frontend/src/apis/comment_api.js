import Send from "@/apis/common_api";

function requestAddComment(data) {
    return Send({
        url   : '/api/comment',
        method: 'POST',
        data  : data
    })
}

function requestGetCommentsByBoardId(data) {
    return Send({
        url   : '/api/comment/' + data,
        method: 'GET'
    })
}

function requestDeleteCommentByCommentId(data) {
    return Send({
        url   : '/api/comment/' + data,
        method: 'DELETE'
    })
}

function requestUpdateComment(data) {
    return Send({
        url   : '/api/comment',
        method: 'PUT',
        data  : data
    })
}

export default {requestAddComment, requestDeleteCommentByCommentId, requestGetCommentsByBoardId, requestUpdateComment};