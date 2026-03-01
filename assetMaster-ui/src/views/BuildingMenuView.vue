<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent,
  NCard, NButton, NSpace, NGrid, NGi, NIcon,
  useMessage,
} from 'naive-ui'
import api from '@/api/axios'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const buildingId = route.params.id as string

interface Building {
  id: number
  buildingName: string
  address: string
  adminName: string
  phone: string
}

const building = ref<Building | null>(null)

async function fetchBuilding() {
  try {
    const res = await api.get(`/buildings/${buildingId}`)
    building.value = res.data.data
  } catch {
    message.error('건물 정보를 불러오는데 실패했습니다.')
  }
}

const menus = [
  {
    key: 'rooms',
    title: '방 관리',
    description: '층별 방 현황 조회 및 세입자 관리',
    icon: '🏠',
    path: 'rooms',
  },
  {
    key: 'calendar',
    title: '달력',
    description: '입금일, 계약 시작/종료일 확인',
    icon: '📅',
    path: 'calendar',
  },
  {
    key: 'info',
    title: '건물 정보',
    description: '건물 기본 정보 조회 및 수정',
    icon: '⚙️',
    path: 'info',
  },
]

onMounted(fetchBuilding)
</script>

<template>
  <NLayout>
    <NLayoutHeader bordered style="padding: 12px 24px; display: flex; align-items: center; gap: 16px">
      <NButton @click="router.push('/')">← 목록</NButton>
      <h2 style="margin: 0">{{ building?.buildingName || '...' }}</h2>
    </NLayoutHeader>
    <NLayoutContent style="padding: 24px; max-width: 800px; margin: 0 auto">
      <p v-if="building?.address" style="color: #666; margin-bottom: 24px">{{ building.address }}</p>
      <NGrid :x-gap="16" :y-gap="16" cols="1 500:3">
        <NGi v-for="menu in menus" :key="menu.key">
          <NCard
            hoverable
            style="cursor: pointer; text-align: center; min-height: 160px; display: flex; align-items: center; justify-content: center"
            @click="router.push(`/building/${buildingId}/${menu.path}`)"
          >
            <div style="font-size: 40px; margin-bottom: 12px">{{ menu.icon }}</div>
            <div style="font-size: 18px; font-weight: bold; margin-bottom: 8px">{{ menu.title }}</div>
            <div style="font-size: 13px; color: #888">{{ menu.description }}</div>
          </NCard>
        </NGi>
      </NGrid>
    </NLayoutContent>
  </NLayout>
</template>
