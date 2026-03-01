import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
})

api.interceptors.request.use((config) => {
  const authStore = useAuthStore()
  if (authStore.accessToken) {
    config.headers.Authorization = `Bearer ${authStore.accessToken}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    const authStore = useAuthStore()

    if (error.response?.status === 401 && !originalRequest._retry && authStore.refreshToken) {
      originalRequest._retry = true
      try {
        const res = await axios.post('http://localhost:8080/api/auth/refresh', null, {
          headers: { Authorization: `Bearer ${authStore.refreshToken}` },
        })
        const { accessToken, refreshToken } = res.data.data
        authStore.setTokens(accessToken, refreshToken)
        originalRequest.headers.Authorization = `Bearer ${accessToken}`
        return api(originalRequest)
      } catch {
        authStore.logout()
        router.push('/login')
      }
    }

    return Promise.reject(error)
  },
)

export default api
