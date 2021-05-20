import board_api from "@/apis/board_api";

const boardStore={
    state:{
        boardList:[],
        boardDetail:''
    },
    mutations:{
        SET_BOARD_LIST(state,payload){
            state.boardList = payload;
        },
        SET_BOARD_DETAIL(state,payload){
            state.boardDetail = payload
        }
    },
    actions:{
        async REQUEST_GET_BOARD(context,payload){
            const response = await board_api.requestGetBoard(payload);
            if(response){
                context.commit('SET_BOARD_DETAIL',response.data);
            }
        },
        async REQUEST_GET_ALL_BOARDS_BY_CATEGORIES(context,payload){
            console.log(payload);
            let tempCategory = JSON.parse(payload);
            let category = [];
            for(let key in tempCategory) {
                if (tempCategory[key]) {
                    category.push(key);
                }
            }
            console.log(category);
            const response = await board_api.requestGetAllBoards(category);
            if(response){
                context.commit('SET_BOARD_LIST',response.data);
                console.log(response.data);
            }
        },
        async REQUEST_ADD_BOARD(context,payload){
            const response = board_api.requestAddBoard(payload);
            if(response){
                context.commit('SET_SNACK_BAR',{
                    msg:'정상적으로 작성되었습니다.', color:'success'
                })
            }
        }

    }

}

export default boardStore