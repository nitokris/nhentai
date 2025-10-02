<template>
  <q-page class="row items-center justify-evenly">
    <q-table :rows="rows"/>
  </q-page>
</template>

<script setup lang="ts">

import {ref} from "vue";

const rows = ref([])

function start_download() {

}

</script>
