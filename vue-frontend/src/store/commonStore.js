const commonStore = {

    state: {
        snackBar: {
            open: false,
            text: '',
            timeout: 2500,
            color: 'error',
            right: false,
            productId:'',
            roomId:'',
            sender:'',
            receiver:'',
            type:''
        },
        dialog: false
    },
    getters: {
        displayedAt(createdAt) {
            createdAt = new Date(createdAt);
            const milliSeconds = new Date() - createdAt
            const seconds = milliSeconds / 1000
            if (seconds < 60) {
                return `방금 전`
            }
            const minutes = seconds / 60
            if (minutes < 60) {
                return `${Math.floor(minutes)}분 전`
            }
            const hours = minutes / 60
            if (hours < 24) {
                return `${Math.floor(hours)}시간 전`
            }
            const days = hours / 24
            if (days < 7) {
                return `${Math.floor(days)}일 전`
            }
            const weeks = days / 7
            if (weeks < 5) {
                return `${Math.floor(weeks)}주 전`
            }
            const months = days / 30
            if (months < 12) {
                return `${Math.floor(months)}개월 전`
            }
            const years = days / 365
            return `${Math.floor(years)}년 전`
        }
    },
    mutations: {
        SET_SNACK_BAR(state, payload) {
            state.snackBar.open = true;
            state.snackBar.type = payload.type;
            state.snackBar.text = payload.msg
            state.snackBar.color = payload.color;
            state.snackBar.right = !!payload.right;
            state.snackBar.productId = payload.productId
            state.snackBar.roomId = payload.roomId;
            state.snackBar.sender = payload.sender;
            state.snackBar.receiver = payload.receiver;
        }
    }
}
export default commonStore;