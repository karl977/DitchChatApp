import axios from "axios";
import {getCookie} from "@/util"

axios.interceptors.request.use(
    config => {
        if(config.method === "post"){
            config.headers['X-XSRF-TOKEN'] = getCookie("XSRF-TOKEN");
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// @ts-ignore
window.axios = axios;

export default axios;