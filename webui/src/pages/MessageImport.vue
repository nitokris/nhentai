<script setup lang="ts">

import {ref} from "vue";
import {api} from "boot/axios";
import {Notify} from "quasar";
import {useRouter} from "vue-router";

const router = useRouter()

const detailUrl = ref('')

function submitUrl() {
  api.post('/api/work/url', {url: detailUrl.value})
    .then(resp => {
      if (resp.status !== 200) {
        Promise.reject(resp.statusText)
      }
      const id = resp.data.id
      Notify.create({
        type: 'positive',
        message: '操作成功',
        position: 'top',
        timeout: 3000
      })
      // router.push({path: `/detail/${id}`})
    }).catch(error => {
    Notify.create({
      type: 'negative', // 错误提示
      message: error,
      position: 'top',  // 显示在顶部
      timeout: 3000     // 3秒后自动消失
    })
  })
}

</script>

<template>

  <q-page padding>
    <div class="row">
      <div class="col-10 offset-1">
        <q-card>
          <q-item>
            <q-btn flat round icon="close"/>
          </q-item>
          <q-separator></q-separator>
          <div class="q-pa-md">
            <q-input type="text" v-model="detailUrl"/>
            <q-btn type="button" @click="submitUrl" color="primary">提交</q-btn>
          </div>
        </q-card>
      </div>
    </div>
  </q-page>

</template>

<style scoped lang="sass">

</style>
