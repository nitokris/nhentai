<script setup lang="ts">

import {ref} from "vue";

const imgs = ref<string[]>([
  'public/000.webp',
  'public/001.webp',
  'public/002.webp',
  'public/003.webp',
])

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


</script>

<template>

  <q-page padding tabindex="1" class="img-read-container" @keyup.left="prevImg" @keyup.right="nextImg">
    <div class="row">
      <div class="col-8 offset-2">
        <q-img class="img-hide" :class="currentIndex===index?'img-read':''" v-for="(item,index) in imgs" :src="item"
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
