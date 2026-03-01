<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent,
  NCard, NForm, NFormItem, NInput, NInputNumber, NButton, NSpace, NDynamicInput, NSwitch,
  useMessage,
} from 'naive-ui'
import api from '@/api/axios'

const router = useRouter()
const message = useMessage()
const loading = ref(false)

const form = ref({
  buildingName: '',
  address: '',
  adminName: '',
  adminPhone: '',
  description: '',
  phone: '',
  parkingAvailable: false,
})

const floors = ref<Array<{ floor: number; roomCount: number }>>([
  { floor: 1, roomCount: 1 },
])

function addFloor() {
  const maxFloor = floors.value.length > 0
    ? Math.max(...floors.value.map((f) => f.floor))
    : 0
  floors.value.push({ floor: maxFloor + 1, roomCount: 1 })
}

function removeFloor(index: number) {
  floors.value.splice(index, 1)
}

async function handleCreate() {
  if (!form.value.buildingName) {
    message.warning('건물 이름을 입력해주세요.')
    return
  }
  if (floors.value.length === 0) {
    message.warning('최소 1개의 층을 추가해주세요.')
    return
  }
  loading.value = true
  try {
    await api.post('/buildings', {
      ...form.value,
      floors: floors.value,
    })
    message.success('건물이 생성되었습니다.')
    router.push('/')
  } catch (err: any) {
    message.error(err.response?.data?.message || '건물 생성에 실패했습니다.')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <NLayout>
    <NLayoutHeader bordered style="padding: 12px 24px; display: flex; align-items: center; gap: 16px">
      <NButton @click="router.push('/')">← 목록</NButton>
      <h2 style="margin: 0">건물 추가</h2>
    </NLayoutHeader>
    <NLayoutContent style="padding: 24px; max-width: 700px; margin: 0 auto">
      <NCard>
        <NForm label-placement="left" label-width="120">
          <NFormItem label="건물 이름" required>
            <NInput v-model:value="form.buildingName" placeholder="건물 이름" />
          </NFormItem>
          <NFormItem label="주소">
            <NInput v-model:value="form.address" placeholder="주소" />
          </NFormItem>
          <NFormItem label="관리자명">
            <NInput v-model:value="form.adminName" placeholder="관리자명" />
          </NFormItem>
          <NFormItem label="관리자 전화번호">
            <NInput v-model:value="form.adminPhone" placeholder="관리자 전화번호" />
          </NFormItem>
          <NFormItem label="운영 전화번호">
            <NInput v-model:value="form.phone" placeholder="운영 전화번호" />
          </NFormItem>
          <NFormItem label="설명">
            <NInput v-model:value="form.description" type="textarea" placeholder="건물 설명" />
          </NFormItem>
          <NFormItem label="주차장 여부">
            <NSwitch v-model:value="form.parkingAvailable">
              <template #checked>있음</template>
              <template #unchecked>없음</template>
            </NSwitch>
          </NFormItem>
        </NForm>

        <h3>층/방 구조 설정</h3>
        <div v-for="(f, idx) in floors" :key="idx" style="display: flex; gap: 12px; align-items: center; margin-bottom: 8px">
          <NInputNumber v-model:value="f.floor" :min="-5" :max="100" style="width: 120px">
            <template #prefix>층:</template>
          </NInputNumber>
          <NInputNumber v-model:value="f.roomCount" :min="1" :max="50" style="width: 140px">
            <template #prefix>방:</template>
          </NInputNumber>
          <NButton size="small" type="error" @click="removeFloor(idx)">삭제</NButton>
        </div>
        <NButton dashed block @click="addFloor" style="margin-top: 8px">+ 층 추가</NButton>

        <NSpace justify="end" style="margin-top: 24px">
          <NButton @click="router.push('/')">취소</NButton>
          <NButton type="primary" :loading="loading" @click="handleCreate">건물 생성</NButton>
        </NSpace>
      </NCard>
    </NLayoutContent>
  </NLayout>
</template>
