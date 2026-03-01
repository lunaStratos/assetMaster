<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent,
  NCard, NButton, NSpace, NGrid, NGi, NEmpty,
  useMessage,
} from 'naive-ui'
import api from '@/api/axios'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()

interface Building {
  id: number
  buildingName: string
  address: string
  adminName: string
  phone: string
}

const buildings = ref<Building[]>([])
const loading = ref(false)

async function fetchBuildings() {
  loading.value = true
  try {
    const res = await api.get('/buildings')
    buildings.value = res.data.data
  } catch {
    message.error('건물 목록을 불러오는데 실패했습니다.')
  } finally {
    loading.value = false
  }
}

function handleLogout() {
  authStore.logout()
  router.push('/login')
}

onMounted(fetchBuildings)
</script>

<template>
  <NLayout>
    <NLayoutHeader bordered style="padding: 12px 24px; display: flex; justify-content: space-between; align-items: center">
      <h2 style="margin: 0">에셋마스터</h2>
      <NSpace>
        <NButton type="primary" @click="router.push('/building/create')">건물 추가</NButton>
        <NButton @click="handleLogout">로그아웃</NButton>
      </NSpace>
    </NLayoutHeader>
    <NLayoutContent style="padding: 24px">
      <NEmpty v-if="!loading && buildings.length === 0" description="등록된 건물이 없습니다." />
      <NGrid :x-gap="16" :y-gap="16" cols="1 600:2 900:3">
        <NGi v-for="b in buildings" :key="b.id">
          <NCard :title="b.buildingName" hoverable style="cursor: pointer" @click="router.push(`/building/${b.id}`)">
            <p v-if="b.address">{{ b.address }}</p>
            <p v-if="b.adminName">관리자: {{ b.adminName }}</p>
            <p v-if="b.phone">연락처: {{ b.phone }}</p>
          </NCard>
        </NGi>
      </NGrid>
    </NLayoutContent>
  </NLayout>
</template>
