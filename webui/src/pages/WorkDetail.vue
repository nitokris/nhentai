<script setup lang="ts">

import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, ref} from "vue";
import {api} from "boot/axios";
import {QTableColumn} from "quasar";

const router = useRouter();
const route = useRoute();

interface Work {
  id: number;
  title: string
  description: string
  resources: string[]
  previews?: string[],
  isFavourite: boolean
  isStar: boolean,
  magnets?: []

}


function goBack() {
  router.back()
}

const detail = ref<Work | null>(null)

const loading = ref(true);

api.get(`/api/work/${route.params.id}`).then(res => {
  console.log(res)
  detail.value = res.data
  console.log(detail.value)
  loading.value = false
})
const handleFavourite = async () => {
  if (detail.value != null) {
    detail.value.isFavourite = !detail.value?.isFavourite;
  }
}

const handleStar = async () => {
  if (detail.value != null) {
    detail.value.isStar = !detail.value.isStar;
  }
}

const slide = ref(0)

const score = ref(0)

const searchMagnet = function () {
  router.push({path: `/magnet/${route.params.id}`})
}

const tab = ref('magnets')

const columns: QTableColumn[] = [

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
  }
];

</script>

<template>

  <q-page padding>
    <div class="row ">
      <div class="col-10 offset-1">
        <q-card>
          <q-item>
            <q-btn flat round icon="close" @click="goBack"/>
            <q-item-section>
              <q-skeleton v-if="loading" type="text"></q-skeleton>
              <div v-else class="text-h5 q-mt-sm q-mb-xs">{{ detail?.title }}</div>
            </q-item-section>
          </q-item>
          <q-separator/>
          <q-card-section horizontal>
            <q-card-section class="col-6">

              <q-carousel v-model="slide"
                          transition-next="slide-left"
                          transition-prev="slide-right"
                          :fullscreen="false" animated swipeable arrow thumbnails infinite>
                <q-skeleton v-if="loading" type="text"></q-skeleton>
                <q-carousel-slide v-else :name="index" v-bind:img-src="item"
                                  v-for="(item,index) in detail?.previews"/>
              </q-carousel>
            </q-card-section>
            <q-card-section class="col-5" vertical>
              <q-card-section class="relative-position" vertical>
                <q-card-section>
                  <span class="text-bold">收藏</span>
                  <q-btn flat icon="star" round outline></q-btn>
                </q-card-section>
                <q-card-section>
                  <span class="text-bold">评分 &nbsp;</span>
                  <q-rating
                    v-model="score"
                    size="2em"
                    color="red-7"
                    icon="favorite"
                    max="5"
                  />
                </q-card-section>
                <q-card-section>
                  <q-btn color="primary" @click="searchMagnet">磁链查找</q-btn>
                </q-card-section>
              </q-card-section>
              <q-separator/>
              <q-skeleton v-if="loading" type="text"></q-skeleton>
              <q-card-section v-else class="text-bold" style="color: #141313"
                              v-html="detail?.description"></q-card-section>
              <q-card-section>

              </q-card-section>
            </q-card-section>
          </q-card-section>

          <q-separator/>
          <q-tabs
            v-model="tab"
            dense
            class="text-grey"
            active-color="primary"
            indicator-color="primary"
            align="justify"
          >
            <q-tab name="magnets" label="MAGNETS"/>
          </q-tabs>
          <q-separator/>
          <q-tab-panels v-model="tab" animated>
            <q-tab-panel name="magnets">
              <q-skeleton v-if="loading"/>
              <div v-else>
                <q-table flat hide-bottom
                         :columns="columns" :rows="detail?.magnets"  row-key="url" :fullscreen="false" :pagination="{ rowsPerPage: 0 }">
                  <template v-slot:body="props">
                    <q-tr>
                      <q-td key="category" :props="props">
                        <q-img :src="'https://sukebei.nyaa.si/'+props.row.category"/>
                      </q-td>
                      <q-td key="title" :props="props">
                        <span>{{props.row.title}}</span>
                      </q-td>
                    </q-tr>
                  </template>
                </q-table>
              </div>
            </q-tab-panel>

          </q-tab-panels>
        </q-card>
      </div>
    </div>

  </q-page>

</template>

<style scoped lang="sass">

</style>
