import {Notify} from "quasar";
import {api} from "boot/axios";

api.interceptors.response.use(resp => resp.data, err => {
  Notify.create({
    type: 'negative',
    message: err.response?.data?.message || '网络错误'
  })
  return Promise.reject(err)
})
