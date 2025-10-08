<script setup lang="ts">
import {onMounted, ref} from 'vue';
import EssentialLink, {type EssentialLinkProps} from 'components/EssentialLink.vue';
import {useRoute, useRouter} from "vue-router";

const linksList: EssentialLinkProps[] = [
  {
    title: 'recent',
    caption: 'recent add',
    icon: 'menu',
    link: '/recent'
  },
  {
    title: 'works',
    caption: 'work list',
    icon: 'menu',
    link: '/works'
  },
  {
    title: 'magnets',
    caption: '',
    icon: 'favorite',
    link: '/magnet'
  },
  {
    title: 'import',
    caption: '',
    icon: 'add',
    link: '/import'
  }
];

const leftDrawerOpen = ref(false);

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value;
}

onMounted(() => {
  leftDrawerOpen.value = false
})

const router = useRouter();
const route = useRoute()

function back(){
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/') // 没有历史记录则回首页
  }
}
</script>

<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />

        <q-toolbar-title>
          NHentai
          <q-btn v-if="route.fullPath.indexOf('recent')<0" @click="back" round flat icon="keyboard_arrow_left"></q-btn>
        </q-toolbar-title>
        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      overlay
    >
      <q-list>
        <q-item-label
          header
        >
          System Functions
        </q-item-label>

        <EssentialLink
          v-for="link in linksList"
          :key="link.title"
          v-bind="link"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>
