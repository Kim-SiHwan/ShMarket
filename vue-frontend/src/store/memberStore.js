import member_api from "@/apis/member_api";
import auth from "@/util/auth"
import router from "@/routes/index";

const memberStore = {
    state: {
        token: sessionStorage.getItem('access_token'),
        authenticated: false,
        username: '',
        nickname: '',
        area: '',
        productCategories: {
            '디지털/가전': false,
            '가구/인테리어': true,
            '유아동/유아도서': true,
            '생활/가공식품': true,
            '스포츠/레저': true,
            '여성잡화': true,
            '여성의류': true,
            '남성패션/잡화': true,
            '게임/취미': true,
            '뷰티/미용': true,
            '반려동물 용품': true,
            '도서/티켓/음반': true,
            '식물': true,
            '기타 중고물품': true
        },
        boardCategories: {
            '우리동네질문': true,
            '분실/실종센터': true,
            '동네사건사고': true,
            '일상': true,
            '동네소식': true,
            '취미생활': true,
            '강아지': true,
            '고양이': true,
            '건강': true,
            '살림': true,
            '인테리어': true,
            '교육/학원': true,
            '동네사진전': true,
            '같이해요': true,
            '출산/육아': true,
            '기타': true
        }
    },
    getters: {
        isAuthenticated(state) {
            return !!(state.token && state.authenticated && state.nickname);
        }
    },
    mutations: {
        LOGIN(state, payload) {
            auth.setStorage(payload);
            state.token = sessionStorage.getItem('access_token');
            state.nickname = sessionStorage.getItem('nickname');
            state.area = sessionStorage.getItem('area');
            state.authenticated = true;
        },
        LOGOUT(state) {
            auth.initStorage();
            state.token = "";
            state.username = "";
            state.authenticated = false;
        },
        INIT_CATEGORIES(state) {
            localStorage.setItem('productCategories', JSON.stringify(state.productCategories));
            localStorage.setItem('boardCategories', JSON.stringify(state.boardCategories));
        }
    },
    actions: {
        async REQUEST_JOIN(context, payload) {
            const joinResponse = await member_api.requestJoin(payload);
            if (joinResponse) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.username + '으로 정상 가입되었습니다.', color: 'success'
                });
                context.commit('INIT_CATEGORIES');
                router.push('/login');
            }
        },
        async REQUEST_LOGIN(context, payload) {
            const loginResponse = await member_api.requestLogin(payload);
            if (loginResponse) {
                context.commit('LOGIN', loginResponse.data);
                context.commit('SET_SNACK_BAR', {
                    msg: loginResponse.data.username + '님 반갑습니다.', color: 'success'
                });
                // router.push('/main');
            }
        },
        async REQUEST_LOGOUT(context) {
            const logoutResponse = await member_api.requestLogout();
            if (logoutResponse) {
                context.commit('LOGOUT');
                context.commit('SET_SNACK_BAR', {
                    msg: '로그아웃 되었습니다.', color: 'success'
                });
            }

        },
    }

}

export default memberStore