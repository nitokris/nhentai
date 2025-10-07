<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {api} from "boot/axios";

const router = useRouter()
const route = useRoute()
const server = api.defaults.baseURL

const props = defineProps({
  id: String,
  cover: String,
  tags:Array<String>,
  title:String
})

const toWorkDetail = (id: string) => {
  debugger
  sessionStorage.setItem('work_list_query', JSON.stringify(route.query));
  router.push({path: `/detail/${id}`})
}


</script>

<template>
  <div class="col-xs-12 col-sm-4 col-md-3 col-lg-2 col-xl-2">
    <q-card @click="toWorkDetail(props.id)" class="fit">
      <q-img :src="server+props.cover"/>
      <q-card-section slot="title">
        <span class="title">{{ props.title }}</span>
      </q-card-section>
      <q-separator/>
      <q-card-section>
        <q-chip v-for="(tag,index) in props.tags" :key="index">
          {{ tag }}
        </q-chip>
      </q-card-section>
    </q-card>
  </div>
</template>

<style scoped lang="sass">
.title
  font-weight: bold

</style>
