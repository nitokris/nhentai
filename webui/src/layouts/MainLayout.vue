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
          Quasar App
        </q-toolbar-title>

        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
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
      <!--      <ComicRead/>-->
      <router-view/>
      <!--            <WorkDetail/>-->
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import EssentialLink, {type EssentialLinkProps} from 'components/EssentialLink.vue';
import WorkDetail from "pages/WorkDetail.vue";
import ComicRead from "pages/ComicRead.vue";

const linksList: EssentialLinkProps[] = [
  {
    title: 'recent',
    caption: 'recent add',
    icon: 'menu',
    link: '/recent'
  },
  {
    title: 'magnets',
    caption: '',
    icon: 'favorite',
    link: '/magnet'
  }, {
    title: 'detail',
    caption: '',
    icon: 'favorite',
    link: '/detail'
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
</script>
