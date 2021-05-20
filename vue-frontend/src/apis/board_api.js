import Send from "@/apis/common_api";

function requestAddBoard(data){
    return Send({
        url:'/api/board',
        method:'POST',
        headers:{'Content-Type': 'multipart/form-data'},
        data:data
    })
}

function requestGetAllBoards(data){
    return Send({
        url:'/api/board',
        method:'GET',
        params:{
            'list':encodeURI(data)
        }
    })
}

function requestGetBoard(data){
    return Send({
        url:'/api/board/'+data,
        method:'GET'
    })
}

export default {requestAddBoard,requestGetAllBoards,requestGetBoard};