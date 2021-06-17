import axios from "axios";
import store from "@/store/store";
import router from "@/routes/index";

const instance = axios.create();

instance.interceptors.request.use(
    async config => {
        const token = sessionStorage.getItem("access_token");
        config.headers = {'Authorization': token}
        return config;
    },
    error => {
        console.log(error);
    }
)

instance.interceptors.response.use(
    response => {
        return response;
    },
    async error => {
        const code = error.response.data.code;
        const msg = error.response.data.message;
        const status = error.response.status;
        console.log(code + " " + msg + " " + status);

        if (code === 1) {
            store.commit('SET_SNACK_BAR', {
                msg: msg, color: 'error'
            })
        } else if (code === 2) {
            store.commit('SET_SNACK_BAR', {
                msg: msg, color: 'error'
            })
        } else if (code === 3) {
            store.commit('SET_SNACK_BAR', {
                msg: msg, color: 'error'
            })
        } else if (code === 4) {
            store.commit('SET_SNACK_BAR', {
                msg: msg, color: 'error'
            })
            await router.push('/error');
        } else if (code === 5) {
            store.commit('SET_SNACK_BAR', {
                msg: msg, color: 'error'
            })
        } else if (code === 10) {
            store.commit('SET_SNACK_BAR', {
                msg: msg, color: 'error'
            })
        }
    }
)

export default instance;