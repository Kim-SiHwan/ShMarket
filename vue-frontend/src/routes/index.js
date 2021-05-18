import Vue from 'vue';
import Router from 'vue-router';
import Main from "@/components/common/Main";
import Join from "@/components/member/Join";
import Login from "@/components/member/Login";
import ProductList from "@/components/product/ProductList";
import AddProduct from "@/components/product/AddProduct";

Vue.use(Router); //vue 라우터 사용


export default new Router({ //라우터 연결
    routes: [
        {
            path:'/',
            component: Main
        },
        {
            path:'/join',
            component: Join
        },
        {
            path:'/login',
            component: Login
        },
        {
            path:'/productList',
            component: ProductList
        },
        {
            path:'/addProduct',
            component: AddProduct
        }
    ]
})