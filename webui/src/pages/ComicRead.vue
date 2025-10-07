<script setup lang="ts">

import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {api} from "boot/axios";

const loading = ref(true)

const router = useRouter()

interface FileEntity {
  fileName: string,
  originalName: string,
  readOrder: number
}

const imgs = ref<FileEntity[]>([])

const route = useRoute()

api.get(`/api/work/resource/${route.params.fileHash}`)
  .then(resp => {
    imgs.value = resp.data
    console.log(resp.data)
    loading.value = false
  })

const currentIndex = ref(0)

function prevImg() {
  if (currentIndex.value <= 0) {
    return
  }
  currentIndex.value = currentIndex.value - 1
}

function nextImg() {
  if (currentIndex.value === imgs.value.length - 1) {
    return
  }
  currentIndex.value = currentIndex.value + 1
}

const swap = function (e) {
  if (e.direction == 'right') {
    prevImg()
  } else if (e.direction == 'left') {
    nextImg()
  }
}

const fabPos = ref([18, 18])
const draggingFab = ref(false)

function moveFab(ev) {
  draggingFab.value = ev.isFirst !== true && ev.isFinal !== true
  fabPos.value = [
    fabPos.value[0] - ev.delta.x,
    fabPos.value[1] - ev.delta.y
  ]
}

function back(){
  router.back()
}

</script>

<template>

  <q-page padding tabindex="1" class="img-read-container" @keyup.left="prevImg" @keyup.right="nextImg">
    <div class="row">
      <q-skeleton v-if="loading"/>
      <div v-else class="col-lg-8 offset-lg-2 col-sm-12 offset-sm-0 col-xs-12 offset-xs-0 ">
        <q-img v-for="(item,index) in imgs"
               :src="`/static/${item.fileName}`"
               :key="index"></q-img>
      </div>
    </div>
    <q-page-sticky position="bottom-right" :offset="fabPos">
      <q-fab
        icon="material-icons-outlined"
        direction="up"
        color="accent"
        :disable="draggingFab"
        v-touch-pan.prevent.mouse="moveFab"
        @click="back"
      >
      </q-fab>
    </q-page-sticky>
  </q-page>

</template>

<style scoped lang="sass">

.img-read-container


.img-hide
  display: none

.img-read
  display: block !important


</style>
