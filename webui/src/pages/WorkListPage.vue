<script setup lang="ts">

import {onMounted, ref} from "vue";
import {api} from "boot/axios";
import {scroll} from "quasar";
import setVerticalScrollPosition = scroll.setVerticalScrollPosition;
import {useRoute, useRouter} from "vue-router";


interface Work {
  id: string;
  title: string;
  description?: string;
  cover?: string;
}

const router = useRouter()
const route = useRoute()

const loading = ref(false)

const page = ref(Number(route.query.page || 1))
const size = ref(Number(route.query.size || 20))
const totalPage = ref(0)


const works = ref<Work[]>([
  {
    id: '1',
    title: '233'
  }
])

onMounted(() => {
  if (!route.query.page) {
    const saved = sessionStorage.getItem('work_list_query');
    if (saved) {
      try {
        const q = JSON.parse(saved);
        if (q.page) {
          // 将 saved 的 query 写回 URL（不产生额外历史）
          router.replace({ path: route.path, query: q }).catch(()=>{});
        }
      } catch (e) { /* ignore */ }
    }
  }
  api.get(`/api/work?page=${page.value}&pageSize=${size.value}`)
    .then(resp => {
      const {data, pagination} = resp.data
      page.value = pagination.page
      totalPage.value = pagination.totalPage
      console.log(data)
      console.log(pagination)
      works.value = data
    })
})

const pageChange = (newPage: number) => {
  router.replace({
    query: {
      ...route.query,
      page: newPage,
      size: size.value
    }
  })
  api.get(`/api/work?page=${newPage}&pageSize=20`)
    .then(resp => {
      const {data, pagination} = resp.data
      page.value = pagination.page
      totalPage.value = pagination.totalPage
      works.value = data
      const el = document.scrollingElement || document.documentElement
      setVerticalScrollPosition(el, 0, 300) // 300ms 平滑滚动到顶部
    })
}

const toWorkDetail = (id: string) => {
  sessionStorage.setItem('work_list_query', JSON.stringify(route.query));
  router.push({path: `/detail/${id}`})
}

</script>

<template>

  <q-page padding>
    <q-skeleton v-if="loading"/>
    <template v-else>
      <div class="row">
        <div class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3" v-for="(item,index) in works" :key="index">
          <q-card @click="toWorkDetail(item.id)">
            <q-card-section>
              <q-img :src="item.cover"/>
            </q-card-section>
            <q-card-section slot="title">
              {{ item.title }}
            </q-card-section>
          </q-card>
        </div>
        <div class="flex flex-center col-12">
          <q-pagination :max="totalPage" :model-value="page" @update:model-value="pageChange"/>
        </div>
      </div>
    </template>

  </q-page>

</template>

<style scoped lang="sass">

</style>
