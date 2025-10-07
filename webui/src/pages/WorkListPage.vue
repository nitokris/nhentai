<script setup lang="ts">

import {onMounted, ref} from "vue";
import {api} from "boot/axios";
import {useRoute, useRouter} from "vue-router";
import WorkListItem from "pages/WorkListItem.vue";


interface Work {
  id: string;
  title: string;
  description?: string;
  cover: string;
  tags?: string[];
}

const router = useRouter()
const route = useRoute()

const loading = ref(true)

const page = ref(Number(route.query.page || 1))
const size = ref(Number(route.query.size || 30))
const options = [
  {
    value: 'recordDate',
    label: '录入时间'
  }, {
    value: 'publishDate',
    label: '发售时间'
  }
]
const sort = ref(route.query.sort || options[0].value)
const totalPage = ref(0)


const works = ref<Work[]>([])

function loadData() {
  loading.value = true
  console.log(sort.value)
  debugger
  if (!route.query.page) {
    const saved = sessionStorage.getItem('work_list_query');
    if (saved) {
      try {
        const q = JSON.parse(saved);
        if (q.page) {
          // 将 saved 的 query 写回 URL（不产生额外历史）
          router.replace({path: route.path, query: q}).catch(() => {
          });
        }
      } catch (e) { /* ignore */
      }
    }
  }
  api.get(`/api/work?page=${page.value}&pageSize=${size.value}&sort=${sort.value}`)
    .then(resp => {
      const {data, pagination} = resp.data
      page.value = pagination.page
      totalPage.value = pagination.totalPage
      console.log(data)
      console.log(pagination)
      works.value = data
    }).finally(() => {
    loading.value = false
  })
}

onMounted(() => {
  loadData();
})

const pageChange = (newPage: number) => {
  page.value = newPage
  router.replace({
    query: {
      ...route.query,
      page: newPage,
      size: size.value,
      sort: sort.value
    }
  })
  loadData()
}


const change = function () {
  page.value = 1
  router.replace({
    query: {
      ...route.query,
      page: page.value,
      size: size.value,
      sort: sort.value
    }
  })
  loadData()
}


</script>

<template>

  <q-page padding class="page-with-footer">
    <q-skeleton v-if="loading"/>
    <template v-else>
      <div class="row justify-between q-mb-md q-mr-sm">
        <q-select rounded outlined v-model="sort" :options="options" label="排序" emit-value map-options
                  @update:model-value="change"/>
        <q-btn-group>
        </q-btn-group>
      </div>
      <div class="row row q-col-gutter-x-sm q-col-gutter-y-lg">
        <WorkListItem v-for="(item,index) in works" v-bind="item" :key="index"/>
        <div class="pagination-footer">
          <q-pagination :max="totalPage" :model-value="page" @update:model-value="pageChange"/>
        </div>
      </div>
    </template>

  </q-page>

</template>

<style scoped lang="sass">
.page-with-footer
  /* 关键：为分页预留高度空间，防止最后一行被挡 */
  padding-bottom: 72px

.page-with-header
  padding-top: 72px

.pagination-footer
  position: fixed
  bottom: 0
  left: 0
  width: 100%
  background: white
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.05)
  padding: 8px 0
  display: flex
  justify-content: center
  z-index: 1000

</style>
