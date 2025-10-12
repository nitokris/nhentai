import {api} from "boot/axios";
import {Work} from "src/api/types/work";

export const recent = (count: number) => api.get<Array<Work>>(`/work/recent?count=${count}`)

export const workDetail = (id: string) => api.get<Work>(`/work/${id}`)


