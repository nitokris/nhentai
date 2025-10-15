<script setup lang="ts">

import MagnetTable from "components/MagnetTable.vue";
import {onMounted, ref} from "vue";
import {search} from "src/api/magnet";
import {dom} from "quasar";
import height = dom.height;

const rows = ref([])

const loading = ref(true)

const pagination = ref({
  page: 1,
  rowsPerPage: 75,
  rowsNumber: 0
});

const filter = ref('')



const queryMagnet = async function ( props: any) {
  loading.value = true;
  try {
    const filterStr = filter.value.trim().replace('[', '').replace(']', '')
    const searchResult = await search(filterStr, props.page)
    rows.value = searchResult.data
    pagination.value.page = searchResult.pagination.page;
    pagination.value.rowsPerPage = 75;
    pagination.value.rowsNumber = searchResult.pagination.total;
  } finally {
    loading.value = false;
  }
}

onMounted(async () => {
  await queryMagnet( pagination.value)
})


</script>

<template>

  <q-page padding>
    <MagnetTable :rows="rows" v-model:loading="loading" v-model:pagination="pagination"
                 @request="queryMagnet"
                 :flat="false"
                 :style="`height: 85vh;max-height: 85vh;`"
    >
      <template v-slot:top-right>
        <q-input borderless dense debounce="300" v-model="filter" placeholder="搜索">
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
