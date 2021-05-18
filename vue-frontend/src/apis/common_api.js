import axios from "axios";
const instance = axios.create();

instance.interceptors.request.use(
    async config=>{
        const token = sessionStorage.getItem("access_token");
        config.headers={'Authorization' : token}
        return config;
    },
    error => {
        console.log(error);
    }
)

instance.interceptors.response.use(
    response=>{
        return response;
    },
    async error=>{
        const code = error.response.data.code;
        const msg = error.response.data.message;
        const status = error.response.status;
        console.log(code+" "+msg+" "+status);

    }

)

export default instance;