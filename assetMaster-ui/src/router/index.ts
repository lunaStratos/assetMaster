import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/signup',
      name: 'Signup',
      component: () => import('@/views/SignupView.vue'),
    },
    {
      path: '/',
      name: 'Buildings',
      component: () => import('@/views/BuildingListView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/building/create',
      name: 'BuildingCreate',
      component: () => import('@/views/BuildingCreateView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/building/:id',
      name: 'BuildingMenu',
      component: () => import('@/views/BuildingMenuView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/building/:id/rooms',
      name: 'Dashboard',
      component: () => import('@/views/DashboardView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/building/:id/calendar',
      name: 'Calendar',
      component: () => import('@/views/CalendarView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/building/:id/info',
      name: 'BuildingInfo',
      component: () => import('@/views/BuildingInfoView.vue'),
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    return { name: 'Login' }
  }
})

export default router
