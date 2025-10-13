import {api} from "boot/axios";
import {Work} from "src/api/types/work";

export const recent = async (count: number): Promise<Work[]> => await api.get<Array<Work>>(`/work/recent?count=${count}`)

export const workDetail = async (id: string): Promise<Work> => await api.get<Work>(`/work/${id}`)


