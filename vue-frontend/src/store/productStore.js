import product_api from "@/apis/product_api";

const productStore = {
    state: {
        productList: [],
        productDetail: '',
    },

    mutations: {
        SET_PRODUCT_LIST(state, payload) {
            state.productList = payload;
        },
        SET_PRODUCT_DETAIL(state, payload) {
            state.productDetail = payload;
        },
    },
    actions: {
        async REQUEST_GET_PRODUCT(context, payload) {
            const response = await product_api.requestGetProduct(payload);
            if (response) {
                context.commit('SET_PRODUCT_DETAIL', response.data);
            }
        },

        async REQUEST_GET_ALL_PRODUCTS_BY_CATEGORIES(context, payload) {
            let tempCategory = JSON.parse(payload);
            let category = [];
            for (let key in tempCategory) {
                if (tempCategory[key]) {
                    category.push(key);
                }
            }
            console.log("리스트 : " + category)
            const response = await product_api.requestGetAllProducts(category);
            if (response) {
                context.commit('SET_PRODUCT_LIST', response.data);
            }
        },
        async REQUEST_ADD_PRODUCT(context, payload) {
            const response = await product_api.requestAddProduct(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '상품을 등록했습니다.', color: 'success'
                });
            }
        },
        async REQUEST_PUSH_INTEREST(context, payload) {
            const response = await product_api.requestInterested(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: response.data, color: 'info'
                })
            }
            const getProductResponse = await product_api.requestGetProduct(payload);
            if (getProductResponse) {
                context.commit('SET_PRODUCT_DETAIL', getProductResponse.data);
            }
        }
    }
}

export default productStore;