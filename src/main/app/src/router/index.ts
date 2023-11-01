import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Page from '../views/Page.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import StreamView from '../views/StreamView.vue'
import axios from 'axios'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main-page',
      component: Page,
      children: [
        {
          path: '',
          name: 'dashboard',
          component: HomeView
        },
        {
          path: 'stream/:id',
          name: 'stream',
          component: StreamView
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    }
  ]
})

router.beforeEach(function (to, from, next) {
  axios('/api/user/logged_in').then(() => {
    if(['/login', '/register'].includes(to.path)){
      next({path: "/"})
    }else{
      next()
    }
  }).catch(() => {
    if(!['/login', '/register'].includes(to.path)){
      next({path: "/login"})
    }else{
      next()
    }
  })
});

export default router
