<script setup lang="ts">

import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, ref} from "vue";
import {api} from "boot/axios";

const router = useRouter();
const route = useRoute();

interface Work {
  id: number;
  metaData: WorkMetaData,
  isFavourite: boolean
  isStar: boolean
}

interface WorkMetaData {
  title: string
  description: string
  resources: string[]

  previews?: string[]
}


function goBack() {
  router.back()
}

const detail = ref<Work | null>(null)

const loading = ref(true);

api.get(`/api/work/${route.params.id }`).then(res => {
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
              <div v-else class="text-h5 q-mt-sm q-mb-xs">{{ detail?.metaData.title }}</div>
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
                                  v-for="(item,index) in detail?.metaData.previews"/>
              </q-carousel>
            </q-card-section>
            <q-card-section class="col-5" vertical>
              <q-skeleton v-if="loading" type="text"></q-skeleton>
              <q-card-section v-else class="text-bold" style="color: #141313"
                              v-html="detail?.metaData.description"></q-card-section>
              <q-separator/>
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
              </q-card-section>
              <q-card-section>

              </q-card-section>
            </q-card-section>
          </q-card-section>

          <q-separator/>
        </q-card>
      </div>
    </div>

  </q-page>

</template>

<style scoped lang="sass">

</style>
