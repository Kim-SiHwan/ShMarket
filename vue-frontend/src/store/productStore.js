import product_api from "@/apis/product_api";
import router from '@/routes/index';

const productStore = {
    state: {
        productList         : [],
        productDetail       : '',
        myProductList       : [],
        myLikeProductList   : [],
        followProductList   : [],
        productTotalPage    : 0,
        productCurrentPage  : 1,
        myProductTotalPage  : 1,
        myProductCurrentPage: 1
    },

    mutations: {
        SET_PRODUCT_LIST(state, payload) {
            state.productList = payload;
        },
        SET_PRODUCT_DETAIL(state, payload) {
            state.productDetail = payload;
        },
        SET_MY_PRODUCT_LIST(state, payload) {
            state.myProductList = payload;
        },
        SET_MY_LIKE_PRODUCT_LIST(state, payload) {
            state.myLikeProductList = payload;
        },
        SET_FOLLOW_PRODUCT_LIST(state, payload) {
            state.followProductList = payload;
        },
        SET_PRODUCT_TOTAL_PAGE(state, payload) {
            console.log(payload);
            state.productTotalPage = payload;
        },
        SET_PRODUCT_CURRENT_PAGE(state, payload) {
            state.productCurrentPage = payload;
        },
        SET_MY_PRODUCT_TOTAL_PAGE(state, payload) {
            state.myProductTotalPage = payload;
        },
        SET_MY_PRODUCT_CURRENT_PAGE(state, payload) {
            state.myProductCurrentPage = payload;
        }
    },
    actions  : {
        async REQUEST_GET_PRODUCT(context, payload) {
            const response = await product_api.requestGetProduct(payload);
            if (response) {
                context.commit('SET_PRODUCT_DETAIL', response.data);
            }
        },
        async REQUEST_ADD_PRODUCT(context, payload) {
            const response = await product_api.requestAddProduct(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '상품을 등록했습니다.', color: 'info'
                });
            }
        },
        async REQUEST_PUSH_INTEREST(context, payload) {
            const response = await product_api.requestInterested(payload);
            if (response) {
                let message = '관심상품에 등록되었습니다.';
                if (!response.data) {
                    message = '관심상품에서 해제되었습니다.';
                }
                context.commit('SET_SNACK_BAR', {
                    msg: message, color: 'info'
                });
            }
        },
        async REQUEST_DELETE_PRODUCT(context, payload) {
            const response = await product_api.requestDeleteProduct(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '정상적으로 삭제되었습니다.', color: 'info'
                });
                await router.push('/productList');
            }
        },
        async REQUEST_UPDATE_PRODUCT(context, payload) {
            const response = await product_api.requestUpdateProduct(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '정상적으로 수정되었습니다.', color: 'info'
                });
            }
        },
        async REQUEST_CHANGE_STATUS(context, payload) {
            const response = await product_api.requestUpdateStatus(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: '상태가 변경되었습니다.', color: 'info'
                });
            }
        },
        async REQUEST_GET_MY_PRODUCT(context, payload) {
            const response = await product_api.requestGetMyProduct(payload);
            if (response) {
                context.commit('SET_MY_PRODUCT_LIST', response.data.data);
                context.commit('SET_MY_PRODUCT_TOTAL_PAGE', response.data.totalPage);
            }
        },
        async REQUEST_GET_MY_LIKE_PRODUCT(context, payload) {
            const response = await product_api.requestGetMyLikeProduct(payload);
            if (response) {
                context.commit('SET_MY_LIKE_PRODUCT_LIST', response.data);
            }
        },
        async REQUEST_GET_ALL_PRODUCTS_PAGES(context, payload) {
            let tempCategory = JSON.parse(payload.category);
            let category = [];
            for (let key in tempCategory) {
                if (tempCategory[key]) {
                    category.push(key);
                }
            }
            let data = {
                page    : payload.page - 1,
                category: category
            }
            const response = await product_api.requestGetAllProductsPage(data);
            if (response) {
                console.log(response);
                context.commit('SET_PRODUCT_LIST', response.data.data);
                context.commit('SET_PRODUCT_TOTAL_PAGE', response.data.totalPage);

            }
        },
    }
}

export default productStore;