import { RouteConfig } from 'vue-router';

import Layout from '@/views/Layout.vue';

import Home from '@/views/Home.vue';
import Module from '@/views/Module.vue';
import Detail from '@/views/Detail.vue';

import Login from '@/views/Login.vue';
import NotFound from '@/views/NotFound.vue';

const routes: Array<RouteConfig> = [
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '/',
        component: Home,
        children: [
          {
            path: '/',
            name: 'module',
            component: Module
          },
          {
            path: 'module/:code',
            name: 'detail',
            component: Detail
          }
        ]
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/*',
    name: '404',
    component: NotFound
  }
];

export default routes;
