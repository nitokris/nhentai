<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {api} from 'boot/axios';
import {useClipboard} from '@vueuse/core';
import {QTableColumn} from "quasar";
import {useRoute, useRouter} from "vue-router";

type SearchResult = {
  url: string;
  title: string;
  type: string;
  size: string;
  date: string;
  isDownloaded: string;
  [key: string]: string;
};

const rows = ref<SearchResult[]>([]);

const router = useRouter()
const route = useRoute()

const {copy} = useClipboard();

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


const selected = ref<SearchResult[]>([]);
const filter = ref(route.query.keyword||'');

const pagination = ref({
  page: 1,
  rowsPerPage: 75,
  rowsNumber: 0
});

async function search() {
  await onRequest({pagination: {page: 1}})
}

async function copySelected() {
  const arr: string[] = [];
  selected.value.forEach((item: any) => {
    arr.push(item.magnet);
  });
  console.log(arr.join('\n'));
  await copy(arr.join('\n'));
}

async function startdownload() {
  const arr: {}[] = [];
  selected.value.forEach(item => {
    // str += `${item.magnet}\n`;
    arr.push({
      title: item.title,
      url: item.url,
      size: item.size,
      date: item.date,
      category: item.category
    });
  });
  console.log(arr);
  await api.post(`/api/magnet`, {
    magnetMetaData: arr
  }).then(resp => {
    if (resp.status === 200) {
      console.log(resp.data)
      const arr = []
      resp.data.forEach((magnetId: object) => {
        arr.push(magnetId[`id`])
      })
      api.put(`/api/work/${workId}/magnets`, arr).then(resp => {
        console.log(resp.status)
      })
    }
  })
  selected.value = [];
}

const tableRef = ref();

onMounted(() => {
  tableRef.value.requestServerInteraction();
});


const loading = ref(false);

async function onRequest(props: any) {
  const {page} = props.pagination;
  loading.value = true;
  console.log(filter.value.trim().replace('[', '').replace(']', ''));
  await api.get(`/api/magnet/search?keyword=${filter.value.trim().replace('[', '').replace(']', '')}&page=${page}`).then((res) => {
    rows.value = res.data.data;
    pagination.value.page = page;
    pagination.value.rowsPerPage = 75;
    pagination.value.rowsNumber = res.data.pagination.total;
    // selected.value = [];
    // console.log(res.data);
  }).finally(() => {
    loading.value = false;
  });
}

function selectFast() {
  rows.value.forEach((item: any) => {
    if (item.title.includes(filter.value) && item.isDownloaded !== 'true') {
      selected.value.push(item);
    }
  });
}


const workId = route.params.workId

</script>

<template>
  <q-card>
    <template v-slot:default>
      <q-table ref="tableRef" :rows="rows" :columns="columns" v-model:selected="selected" selection="multiple"
               row-key="url"
               v-model:loading="loading"
               :fullscreen="false"
               class="my-sticky-header-table"
               style="height: 85vh;max-height: 85vh;"
               v-model:pagination="pagination" @request="onRequest">

        <template v-slot:top-left>
          <q-btn color="primary" @click="copySelected">
            拷贝
          </q-btn>
          <q-btn color="primary" @click="startdownload">
            发送下载
          </q-btn>
          <q-btn color="primary" @click="selectFast">
            快速选中
          </q-btn>
        </template>

        <template v-slot:top-right>
          <q-input borderless dense debounce="300" v-model="filter" placeholder="搜索">
            <template v-slot:append>
              <q-btn color="primary" @click="search">
                <q-icon name="search"/>
              </q-btn>

            </template>
          </q-input>
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
              <div v-html="props.row.title.replace(filter,`<span class='text-red'>${filter}</span>`)"></div>
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
