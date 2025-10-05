import type {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/recent',   // 🚀 默认进入 /recent
  },
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {path: '/magnet', component: () => import('pages/MagnetPage.vue')},
      {path: '/magnet/:workId', component: () => import('pages/MagnetPage.vue')},
      {path: '/recent', component: () => import('pages/RecentAdd.vue')},
      {path: '/detail/:id', component: () => import('pages/WorkDetail.vue')},
      {path: '/import', component: () => import('pages/MessageImport.vue')},
      {path: '/read/:fileHash', component: () => import ('pages/ComicRead.vue')}
    ]
  },


  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
];

export default routes;
