import Vue from "vue";
import Vuex from "vuex";
import commonStore from "@/store/commonStore";
import memberStore from "@/store/memberStore";
import areaStore from "@/store/areaStore";
import productStore from "@/store/productStore";
import boardStore from "@/store/boardStore";
Vue.use(Vuex);

export const store = new Vuex.Store({
    // ...
    modules:{
        commonStore,
        memberStore,
        areaStore,
        productStore,
        boardStore

    }

});
export default store;