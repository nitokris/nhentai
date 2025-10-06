<script setup lang="ts">

import {ref} from "vue";
import {useRoute} from "vue-router";
import {api} from "boot/axios";

const loading = ref(true)

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

</script>

<template>

  <q-page padding tabindex="1" class="img-read-container" @keyup.left="prevImg" @keyup.right="nextImg">
    <div class="row">
      <q-skeleton v-if="loading"/>
      <div v-else class="col-lg-8 offset-lg-2 col-sm-12 offset-sm-0 col-xs-12 offset-xs-0 ">
        <q-img  v-for="(item,index) in imgs"
               :src="`/static/${item.fileName}`"
               :key="index"></q-img>
      </div>
    </div>
  </q-page>

</template>

<style scoped lang="sass">

.img-read-container


.img-hide
  display: none

.img-read
  display: block !important


</style>
