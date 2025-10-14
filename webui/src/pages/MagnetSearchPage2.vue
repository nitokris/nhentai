<script setup lang="ts">

import MagnetTable from "components/MagnetTable.vue";
import {onMounted, ref} from "vue";
import {search} from "src/api/magnet";

const rows = ref([])

const loading = ref(true)

const pagination = ref({
  page: 1,
  rowsPerPage: 75,
  rowsNumber: 0
});

const filter = ref('')
const page = 1;

const nextPage = function (props: any) {
  console.log('magnet search page2 next page:', props)
}


const queryMagnet = async function () {
  loading.value = true;
  try {
    const filterStr = filter.value.trim().replace('[', '').replace(']', '')
    const searchResult = await search(filterStr, page)
    rows.value = searchResult.data
    pagination.value.page = page;
    pagination.value.rowsPerPage = 75;
    pagination.value.rowsNumber = searchResult.pagination.total;
  } finally {
    loading.value = false;
  }
}

onMounted(async () => {
  await queryMagnet()
})


</script>

<template>

  <q-page padding>
    <MagnetTable :rows="rows" :loading="loading" v-model:pagination="pagination" @on-page-change="nextPage">
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter" placeholder="搜索" >
          <template v-slot:append>
            <q-btn color="primary" @click="queryMagnet">
              <q-icon name="search"/>
            </q-btn>
          </template>
        </q-input>
      </template>
    </MagnetTable>
  </q-page>

</template>

<style scoped lang="sass">

</style>
