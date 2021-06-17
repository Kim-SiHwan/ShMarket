import Vue from "vue";
import Vuex from "vuex";
import commonStore from "@/store/commonStore";
import memberStore from "@/store/memberStore";
import areaStore from "@/store/areaStore";
import productStore from "@/store/productStore";
import boardStore from "@/store/boardStore";
import commentStore from "@/store/commentStore";
import pushStore from "@/store/pushStore";
import chatStore from "@/store/chatStore";
import noticeStore from "@/store/noticeStore";
import followStore from "@/store/followStore";
import qnaStore from "@/store/qnaStore";

Vue.use(Vuex);

export const store = new Vuex.Store({
    // ...
    modules: {
        commonStore,
        memberStore,
        areaStore,
        productStore,
        boardStore,
        commentStore,
        pushStore,
        chatStore,
        noticeStore,
        followStore,
        qnaStore
    }

});
export default store;