import {api} from "boot/axios";
import {Work} from "src/api/types/work";

export const recent = async (count: number): Promise<Work[]> => {
  const {data} = await api.get<Array<Work>>(`/work/recent?count=${count}`)
  return data
}

export const workDetail = async (id: string): Promise<Work> => {
  const {data} = await api.get<Work>(`/work/${id}`)
  return data
}


