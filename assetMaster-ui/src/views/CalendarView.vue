<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent,
  NCard, NButton, NSpace, NTag, NCalendar,
  useMessage,
} from 'naive-ui'
import api from '@/api/axios'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const buildingId = route.params.id as string

interface Room {
  id: number
  floor: number
  roomNumber: string
  status: string
  tenantName: string | null
  monthlyRent: number | null
  startDate: string | null
  endDate: string | null
}

interface Building {
  id: number
  buildingName: string
}

const building = ref<Building | null>(null)
const rooms = ref<Room[]>([])
const currentValue = ref(Date.now())
const calendarKey = ref(0)

function moveMonth(offset: number) {
  const d = new Date(currentValue.value)
  d.setMonth(d.getMonth() + offset)
  currentValue.value = d.getTime()
  calendarKey.value++
}

function goToday() {
  currentValue.value = Date.now()
  calendarKey.value++
}

function formatDate(ts: number): string {
  const d = new Date(ts)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

// 해당 날짜에 표시할 이벤트 목록
function getEventsForDate(ts: number) {
  const dateStr = formatDate(ts)
  const day = new Date(ts).getDate()
  const events: Array<{ type: string; label: string; color: string }> = []

  rooms.value.forEach((room) => {
    // 계약 시작일
    if (room.startDate === dateStr) {
      events.push({ type: 'start', label: `${room.roomNumber}호 계약시작`, color: '#18a058' })
    }
    // 계약 종료일
    if (room.endDate === dateStr) {
      events.push({ type: 'end', label: `${room.roomNumber}호 계약종료`, color: '#d03050' })
    }
    // 월세 입금일 (매월 계약시작일 기준 동일 일자)
    if (room.startDate && room.monthlyRent && room.status === 'OCCUPIED') {
      const startDay = new Date(room.startDate).getDate()
      if (day === startDay) {
        const rent = room.monthlyRent.toLocaleString()
        events.push({ type: 'rent', label: `${room.roomNumber}호 ${rent}원`, color: '#2080f0' })
      }
    }
  })

  return events
}

async function fetchData() {
  try {
    const [bRes, rRes] = await Promise.all([
      api.get(`/buildings/${buildingId}`),
      api.get(`/calendar/building/${buildingId}`),
    ])
    building.value = bRes.data.data
    rooms.value = rRes.data.data
  } catch {
    message.error('데이터를 불러오는데 실패했습니다.')
  }
}

onMounted(fetchData)
</script>

<template>
  <NLayout>
    <NLayoutHeader bordered style="padding: 12px 24px; display: flex; align-items: center; gap: 16px">
      <NButton @click="router.push(`/building/${buildingId}`)">← 메뉴</NButton>
      <h2 style="margin: 0">{{ building?.buildingName || '...' }} - 달력</h2>
    </NLayoutHeader>
    <NLayoutContent style="padding: 24px">
      <NCard>
        <NSpace justify="space-between" align="center" style="margin-bottom: 16px">
          <NSpace>
            <NTag type="success" size="small">● 계약시작</NTag>
            <NTag type="error" size="small">● 계약종료</NTag>
            <NTag type="info" size="small">● 월세입금일</NTag>
          </NSpace>
          <NSpace>
            <NButton size="small" @click="moveMonth(-12)">◀ 1년</NButton>
            <NButton size="small" @click="moveMonth(-6)">◀ 6개월</NButton>
            <NButton size="small" @click="moveMonth(-3)">◀ 3개월</NButton>
            <NButton size="small" @click="moveMonth(-1)">◀ 1개월</NButton>
            <NButton size="small" type="primary" @click="goToday">오늘</NButton>
            <NButton size="small" @click="moveMonth(1)">1개월 ▶</NButton>
            <NButton size="small" @click="moveMonth(3)">3개월 ▶</NButton>
            <NButton size="small" @click="moveMonth(6)">6개월 ▶</NButton>
            <NButton size="small" @click="moveMonth(12)">1년 ▶</NButton>
          </NSpace>
        </NSpace>
        <NCalendar :key="calendarKey" v-model:value="currentValue" #="{ year, month, date }">
          <div style="min-height: 40px">
            <div
              v-for="(ev, idx) in getEventsForDate(new Date(year, month - 1, date).getTime())"
              :key="idx"
              style="font-size: 11px; padding: 1px 4px; margin-bottom: 2px; border-radius: 3px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis"
              :style="{ backgroundColor: ev.color + '20', color: ev.color }"
            >
              {{ ev.label }}
            </div>
          </div>
        </NCalendar>
      </NCard>
    </NLayoutContent>
  </NLayout>
</template>
