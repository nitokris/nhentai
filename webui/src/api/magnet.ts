import {api} from "boot/axios";
import {Magnet, MagnetId} from "src/api/types/magnet";

export const search = async (filter: string, page: number = 1) => await api.get(`/magnet/search?keyword=${filter}&page=${page}`)

export const record = async (magnets: Array<Magnet>) => await api.post<Array<MagnetId>>('/magnet', {magnetMetaData: magnets})

export const bindToWork = async (workId: string, magnetId: Array<String>) => await api.put(`/work/${workId}/magnets`, magnetId)
