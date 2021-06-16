import Send from "@/apis/common_api";

function requestAddProduct(data) {
    return Send({
        url: '/api/product',
        method: 'POST',
        headers: {'Content-Type': 'multipart/form-data'},
        data: data
    })
}

function requestInterested(data) {
    return Send({
        url: '/api/product/' + data,
        method: 'POST',
    })
}

function requestGetAllProducts(data) {
    console.log(data);
    return Send({
        url: '/api/product',
        method: 'GET',
        params: {
            'list': encodeURI(data)
        }
    })
}

function requestGetProduct(data) {
    return Send({
        url: '/api/product/' + data,
        method: 'GET'
    })
}

function requestDeleteProduct(data) {
    return Send({
        url: '/api/product/' + data,
        method: 'DELETE'
    })
}

function requestUpdateProduct(data) {
    return Send({
        url: '/api/product',
        method: 'PUT',
        headers: {'Content-Type': 'multipart/form-data'},
        data: data
    })
}

function requestUpdateStatus(data) {
    return Send({
        url: '/api/product/status/' + data.productId,
        method: 'PUT',
        params: {status: data.status}
    })
}

function requestGetMyProduct(data) {
    return Send({
        url: '/api/product/my',
        method: 'GET',
        params: {originName: data.originName, requestNickname: data.requestNickname}
    })
}

function requestGetMyLikeProduct(data) {
    return Send({
        url: '/api/product/my/like',
        method: 'GET',
        params: {nickname: data}
    })
}

export default {
    requestAddProduct,
    requestInterested,
    requestGetAllProducts,
    requestGetProduct,
    requestDeleteProduct,
    requestUpdateProduct,
    requestUpdateStatus,
    requestGetMyProduct,
    requestGetMyLikeProduct
};

