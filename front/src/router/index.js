import { createRouter, createWebHistory } from 'vue-router'

//导入组件
import LoginVue from '@/views/Login.vue'
import HomeVue from '@/views/Home.vue'
import ExampleVue from '@/views/Example.vue'
import ArticleVue from '@/views/Article.vue'
import VideoVue from '@/views/Video.vue'
import ArticleDetailVue from '@/views/ArticleDetail.vue'
import VideoDetailVue from '@/views/VideoDetail.vue'


// 定义路由
const routes = [
  {
    path: '/home',
    name: 'Home',
    component: HomeVue
  },
  {
    path: '/login',
    component: LoginVue
  },
  {
    path: '/',
    redirect: '/home' // 重定向到首页
  },
  //   {
  //   path: '/example',
  //   component: ExampleVue
  // },
  {
    path: '/article',
    name: 'Article',
    component: ArticleVue,
    meta: {
      keepAlive: true // 需要被缓存
    }
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: ArticleDetailVue
  },
  {
    path:'/video',
    component: VideoVue
  },
  {
    path: '/video/:id',
    name: 'VideoDetail',
    component: VideoDetailVue
  }

  // 可以添加更多路由
  // {
  //   path: '/dashboard',
  //   name: 'Dashboard',
  //   component: () => import('../views/Dashboard.vue')
  // }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router