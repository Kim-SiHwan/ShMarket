import Send from "@/apis/common_api";

function requestAddProduct(data){
    return Send({
        url:'/api/product',
        method:'POST',
        headers:{'Content-Type': 'multipart/form-data'},
        data:data
    })
}

function requestInterested(data){
    return Send({
        url:'/api/product/'+data,
        method:'POST',
    })
}

function requestGetAllProducts(data){
    console.log(data);
    return Send({
        url:'/api/product',
        method:'GET',
        params :{
            'list' : encodeURI(data)
        }
    })
}

function requestGetProduct(data){
    return Send({
        url:'/api/product/'+data,
        method:'GET'
    })
}

export default {requestAddProduct,requestInterested,requestGetAllProducts,requestGetProduct};