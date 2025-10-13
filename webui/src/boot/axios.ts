import {defineBoot} from '#q-app/wrappers';
import axios, {type AxiosInstance, AxiosRequestConfig} from 'axios';
import {Notify} from "quasar";

declare module 'vue' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance;
    $api: AxiosInstance;
  }
}

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)

const {protocol, hostname, port} = window.location;

const apiBaseUrl = `${protocol}//${hostname}:${port ?? 80}/api`; // 自动使用当前访问的域名或IP
// import.meta.env.VITE_API_BASE_URL
const instance = axios.create({baseURL: apiBaseUrl});
instance.interceptors.response.use(resp => resp.data, err => {
  Notify.create({
    type: 'negative',
    message: err.response?.data?.message || '网络错误'
  })
  return Promise.reject(err)
});

type DataAxiosInstance = Omit<AxiosInstance, 'get' | 'post' | 'put' | 'delete'> & {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T>
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T>
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T>
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T>
}


export default defineBoot(({app}) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api

  app.config.globalProperties.$axios = axios;
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$api = instance;
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
});

export const api = instance as DataAxiosInstance
