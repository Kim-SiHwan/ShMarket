import member_api from "@/apis/member_api";
import auth from "@/util/auth"
import router from "@/routes/index";

const memberStore = {
    state    : {
        token            : sessionStorage.getItem('access_token'),
        authenticated    : false,
        username         : '',
        nickname         : '',
        area             : '',
        emailCode        : '',
        fcmToken         : localStorage.getItem('fcmToken'),
        manners          : [],
        reviews          : [],
        blocks           : [],
        productCategories: {
            '디지털/가전'  : false,
            '가구/인테리어' : true,
            '유아동/유아도서': true,
            '생활/가공식품' : true,
            '스포츠/레저'  : true,
            '여성잡화'    : true,
            '여성의류'    : true,
            '남성패션/잡화' : true,
            '게임/취미'   : true,
            '뷰티/미용'   : true,
            '반려동물 용품' : true,
            '도서/티켓/음반': true,
            '식물'      : true,
            '기타 중고물품' : true
        },
        boardCategories  : {
            '우리동네질문' : true,
            '분실/실종센터': true,
            '동네사건사고' : true,
            '일상'     : true,
            '동네소식'   : true,
            '취미생활'   : true,
            '강아지'    : true,
            '고양이'    : true,
            '건강'     : true,
            '살림'     : true,
            '인테리어'   : true,
            '교육/학원'  : true,
            '동네사진전'  : true,
            '같이해요'   : true,
            '출산/육아'  : true,
            '기타'     : true
        }
    },
    getters  : {
        isAuthenticated(state) {
            return !!(state.token && state.authenticated && state.nickname);
        }
    },
    mutations: {
        LOGIN(state, payload) {
            auth.setStorage(payload);
            state.token = sessionStorage.getItem('access_token');
            state.nickname = sessionStorage.getItem('nickname');
            state.username = sessionStorage.getItem('username');
            state.area = sessionStorage.getItem('area');
            state.authenticated = true;
        },
        LOGOUT(state) {
            auth.initStorage();
            state.token = "";
            state.username = "";
            state.nickname = "";
            state.authenticated = false;
        },
        INIT_CATEGORIES(state) {
            localStorage.setItem('productCategories', JSON.stringify(state.productCategories));
            localStorage.setItem('boardCategories', JSON.stringify(state.boardCategories));
        },
        SET_FCM_TOKEN(state, payload) {
            state.fcmToken = payload;
        },
        SET_MANNERS(state, payload) {
            state.manners = payload;
        },
        SET_REVIEWS(state, payload) {
            state.reviews = payload;
        },
        SET_BLOCKS(state, payload) {
            state.blocks = payload;
        },
        SET_EMAIL_CODE(state, payload) {
            state.emailCode = payload;
        }

    },
    actions  : {
        async REQUEST_JOIN(context, payload) {
            const joinResponse = await member_api.requestJoin(payload);
            if (joinResponse) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.username + '으로 정상 가입되었습니다.', color: 'info'
                });
                context.commit('INIT_CATEGORIES');
                await router.push('/login');
            }
        },
        async REQUEST_LOGIN(context, payload) {
            const loginResponse = await member_api.requestLogin(payload);
            if (loginResponse) {
                context.commit('LOGIN', loginResponse.data);
                context.commit('SET_SNACK_BAR', {
                    msg: loginResponse.data.username + '님 반갑습니다.', color: 'info'
                });

                await context.dispatch('REQUEST_GET_CHAT_COUNT', context.state.nickname);
                await context.dispatch('REQUEST_GET_NOTICES', context.state.nickname);
                await context.dispatch('REQUEST_GET_NOTICE_COUNT', context.state.nickname);
                await router.push('/productList');
            }
        },
        async REQUEST_LOGOUT(context) {
            context.commit('LOGOUT');
            context.commit('SET_SNACK_BAR', {
                msg: '로그아웃 되었습니다.', color: 'info'
            });
        },
        async REQUEST_ADD_MANNER(context, payload) {
            const response = await member_api.requestAddManner(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.nickname + '님에 대한 매너 평가가 완료되었습니다.', color: 'info'
                });
            }
        },
        async REQUEST_GET_MANNERS(context, payload) {
            const response = await member_api.requestGetManners(payload);
            if (response) {
                context.commit('SET_MANNERS', response.data);
            }
        },
        async REQUEST_ADD_REVIEW(context, payload) {
            const response = await member_api.requestAddReview(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.nickname + '님과의 거래 후기가 작성되었습니다.', color: 'info'
                });
            }
        },
        async REQUEST_GET_REVIEWS(context, payload) {
            const response = await member_api.requestGetReviews(payload);
            if (response) {
                context.commit('SET_REVIEWS', response.data);
            }
        },
        async REQUEST_ADD_BLOCK(context, payload) {
            const response = await member_api.requestAddBlock(payload);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.toNickname + '님을 차단했습니다 더이상 사용자가 올린 글이 보이지 않습니다.', color: 'info'
                })
            }
        },
        async REQUEST_GET_BLOCKS(context, payload) {
            const response = await member_api.requestGetBlocks(payload);
            if (response) {
                context.commit('SET_BLOCKS', response.data);
            }
        },
        async REQUEST_DELETE_BLOCK(context, payload) {
            const response = await member_api.requestDeleteBlock(payload.blockId);
            if (response) {
                context.commit('SET_SNACK_BAR', {
                    msg: payload.toNickname + '님 차단을 해제했습니다 다시 사용자가 올린 글이 표시됩니다.', color: 'info'
                })
            }
        },
        async REQUEST_SEND_EMAIL(context, payload) {
            const response = await member_api.requestSendEmail(payload);
            if (response) {
                context.commit('SET_EMAIL_CODE', response.data);
            }
        }

    }

}

export default memberStore