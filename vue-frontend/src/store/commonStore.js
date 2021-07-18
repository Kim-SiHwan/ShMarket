const commonStore = {

    state    : {
        snackBar: {
            open     : false,
            text     : '',
            timeout  : 2500,
            color    : 'error',
            right    : false,
            productId: '',
            roomId   : '',
            sender   : '',
            receiver : '',
            type     : '',
            msg      : ''
        },
        dialog  : false
    },
    mutations: {
        SET_SNACK_BAR(state, payload) {
            if (payload.type) {
                if (payload.type === 1) {
                    state.snackBar.text = payload.msg;
                } else {
                    state.snackBar.text = payload.receiver + '님으로부터 ' + payload.sender + '아이디로 메시지 도착';
                    state.snackBar.msg = payload.msg;
                }
            } else {
                state.snackBar.text = payload.msg;
            }
            state.snackBar.open = true;
            state.snackBar.type = payload.type;
            state.snackBar.color = payload.color;
            state.snackBar.right = !!payload.right;
            state.snackBar.productId = payload.productId
            state.snackBar.roomId = payload.roomId;
            state.snackBar.sender = payload.sender;
            state.snackBar.receiver = payload.receiver;
            state.snackBar.msg = payload.msg;
        }
    }
}
export default commonStore;