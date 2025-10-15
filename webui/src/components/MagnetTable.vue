<script setup lang="ts">

import {QPagination, QTableColumn} from "quasar";
import {Magnet} from "src/api/types/magnet";
import {ref, watch} from "vue";

const columns: QTableColumn[] = [
  {
    name: 'isDownloaded',
    required: true,
    label: '是否已下载',
    align: 'center',
    field: (row: any) => row.isDownloaded,
    format: (val: any) => `${val === 'true' ? '是' : '否'}`
  },
  {
    name: 'category',
    required: true,
    label: '类型',
    align: 'left',
    field: (row: any) => row.category,
    format: (val: any) => `${val}`,
    sortable: true
  },
  {
    name: 'title',
    required: true,
    label: '标题',
    align: 'left',
    field: (row: any) => row.title,
    format: (val: any) => `${val}`,
    sortable: true
  }, {
    name: 'size',
    required: true,
    label: '大小',
    align: 'left',
    field: (row: any) => row.size,
    format: (val: any) => `${val}`,
    sortable: true
  }
  , {
    name: 'date',
    required: true,
    label: '时间',
    align: 'left',
    field: (row: any) => row.date,
    format: (val: any) => `${val}`,
    sortable: true
  }
];

interface Props {
  rows: Magnet[]
  loading: boolean,
  pagination: any,
  style: string,
  flat: boolean
}

const props = defineProps<Props>()

const localPagination = ref({...props.pagination})
const localLoading = ref(props.loading)

watch(() => props.pagination, (newVal) => {
  localPagination.value = {...newVal}
}, {deep: true})

watch(() => props.loading, (newVal) => {
  localLoading.value = newVal
}, {deep: true})


const emits = defineEmits(['request'])


function onRequest(props: any) {
  emits('request', props.pagination)
}

</script>

<template>
  <q-card>
    <template v-slot:default>

      <q-table :rows="rows" :columns="columns" selection="multiple"
               row-key="url"
               :flat="flat"
               v-model:pagination="localPagination"
               v-model:loading="localLoading"
               :fullscreen="false"
               class="my-sticky-header-table"
               @request="onRequest"
               :style="style"
      >
        <template v-slot:top-right>
          <slot name="top-right"></slot>
        </template>

        <template v-slot:body="props">
          <q-tr>
            <q-td>
              <q-checkbox v-model="props.selected" name="checkbox"/>
            </q-td>
            <q-td key="isDownloaded" :props="props">
              <span>{{ props.row.isDownloaded === 'true' ? '是' : '否' }}</span>
            </q-td>
            <q-td key="category" :props="props">
              <q-img :src="'https://sukebei.nyaa.si/'+props.row.category"/>
            </q-td>
            <q-td key="title" :props="props">
              <div v-html="props.row.title"></div>
            </q-td>
            <q-td key="size" :props="props">
              <span>{{ props.row.size }}</span>
            </q-td>
            <q-td key="date" :props="props">
              <span>{{ props.row.date }}</span>
            </q-td>
          </q-tr>
        </template>
      </q-table>
    </template>
  </q-card>
</template>

<style scoped lang="sass">

.text-red
  color: red

.my-sticky-header-table
  .q-table__top,
  .q-table__bottom,
  thead tr:first-child th
    /* bg color is important for th; just specify one */
    background-color: white

  thead tr th
    position: sticky
    z-index: 1

  thead tr:first-child th
    top: 0

</style>
