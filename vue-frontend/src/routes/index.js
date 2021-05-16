import Vue from 'vue';
import Router from 'vue-router';
import Main from "@/components/common/Main";

Vue.use(Router); //vue 라우터 사용


export default new Router({ //라우터 연결
    routes: [
        {
            path:'/',
            component: Main
        }
    ]
})