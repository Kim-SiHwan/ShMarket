import member_api from "@/apis/member_api";
import auth from "@/util/auth"
import router from "@/routes/index";

const memberStore={
    state:{
        token:sessionStorage.getItem('access_token'),
        authenticated:false,
        username:'',
        nickname:'',
        area:''
    },
    getters:{
      isAuthenticated(state){
          return !!(state.token && state.authenticated && state.nickname);
      }
    },
    mutations:{
        LOGIN(state,payload){
            auth.setStorage(payload);
            state.token=sessionStorage.getItem('access_token');
            state.nickname=sessionStorage.getItem('nickname');
            state.area=sessionStorage.getItem('area');
            state.authenticated=true;
        },
        LOGOUT(state){
            auth.initStorage();
            state.token="";
            state.username="";
            state.authenticated=false;
        },
    },
    actions:{
        async REQUEST_JOIN(context,payload){
            const joinResponse = await member_api.requestJoin(payload);
            if(joinResponse){
                context.commit('SET_SNACK_BAR',{
                    msg:payload.username+'으로 정상 가입되었습니다.',color:'success'
                });
                router.push('/login');
            }
        },
        async REQUEST_LOGIN(context,payload){
            const loginResponse = await member_api.requestLogin(payload);
            if(loginResponse){
                context.commit('LOGIN',loginResponse.data);
                context.commit('SET_SNACK_BAR',{
                    msg:loginResponse.data.username+'님 반갑습니다.',color:'success'
                });
                // router.push('/main');
            }
        },
        async REQUEST_LOGOUT(context){
            const logoutResponse = await member_api.requestLogout();
            if(logoutResponse) {
                context.commit('LOGOUT');
                context.commit('SET_SNACK_BAR', {
                    msg: '로그아웃 되었습니다.', color: 'success'
                });
            }

        },
    }

}

export default memberStore