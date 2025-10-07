<script setup lang="ts">

import {onMounted, ref} from "vue";
import {QPagination, QPaginationProps, QTableColumn} from "quasar";
import {api} from "boot/axios";


interface Work {
  id: number;
  title: string;
  description: string;
  cover: string;
}

const loading = ref(true);

const works = ref<Work[]>([]);
const rowsPerPage = 8

const pagination: QPagination = {
  rowsPerPage: rowsPerPage
}
onMounted(async () => {
  api.get(`/api/work/recent?count=${pagination.rowsPerPage}`).then(res => {
    console.log(res)
    works.value = res.data
    loading.value = false
  })
})

const columns: QTableColumn[] = [{
  name: 'cover',
  required: true,
  field: row => row.cover,
  label: 'cover',
}]

const server = api.defaults.baseURL

</script>

<template>

  <q-page padding>
    <q-card>
      <q-table title="最近入库" :rows="works" :columns="columns" :fullscreen="false" hide-pagination grid
               row-key="id" :pagination="pagination">
        <template v-slot:item="props">
          <q-skeleton v-if="loading" type="text"/>
          <div v-else class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3">
            <router-link
              :to="`/detail/${props.row.id}`"
              tag="div"

            >
              <q-img :src="server+(props.row.cover)">
              </q-img>
            </router-link>
          </div>
        </template>
      </q-table>
    </q-card>


  </q-page>

</template>

<style scoped lang="sass">

</style>
