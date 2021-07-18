import Send from "@/apis/common_api";

function requestAddProduct(data) {
    return Send({
        url    : '/api/product',
        method : 'POST',
        headers: {'Content-Type': 'multipart/form-data'},
        data   : data
    })
}

function requestInterested(data) {
    return Send({
        url   : '/api/product/' + data,
        method: 'POST',
    })
}

function requestGetAllProductsPage(data) {
    return Send({
        url   : '/api/product/list/' + data.page,
        method: 'GET',
        params: {
            'list': encodeURI(data.category)
        }
    })
}

function requestGetProduct(data) {
    return Send({
        url   : '/api/product/' + data,
        method: 'GET'
    })
}

function requestDeleteProduct(data) {
    return Send({
        url   : '/api/product/' + data,
        method: 'DELETE'
    })
}

function requestUpdateProduct(data) {
    return Send({
        url    : '/api/product',
        method : 'PATCH',
        headers: {'Content-Type': 'multipart/form-data'},
        data   : data
    })
}

function requestUpdateStatus(data) {
    return Send({
        url   : '/api/product/status/' + data.productId,
        method: 'PATCH',
        params: {status: data.status}
    })
}

function requestGetMyProduct(data) {
    return Send({
        url   : '/api/product/my/' + data.page,
        method: 'GET',
        params: {nickname: data.nickname}
        // params: {originName: data.originName, requestNickname: data.requestNickname}
    })
}

function requestGetMyLikeProduct(data) {
    return Send({
        url   : '/api/product/my/like',
        method: 'GET',
        params: {nickname: data}
    })
}

export default {
    requestAddProduct,
    requestInterested,
    requestGetProduct,
    requestDeleteProduct,
    requestUpdateProduct,
    requestUpdateStatus,
    requestGetMyProduct,
    requestGetMyLikeProduct,
    requestGetAllProductsPage
};

