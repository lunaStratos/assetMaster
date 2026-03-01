<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent,
  NCard, NForm, NFormItem, NInput, NButton, NSpace, NPopconfirm, NSwitch,
  useMessage,
} from 'naive-ui'
import api from '@/api/axios'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const buildingId = route.params.id as string
const saving = ref(false)

const form = ref({
  buildingName: '',
  address: '',
  adminName: '',
  adminPhone: '',
  description: '',
  phone: '',
  parkingAvailable: false,
})

async function fetchBuilding() {
  try {
    const res = await api.get(`/buildings/${buildingId}`)
    const b = res.data.data
    form.value = {
      buildingName: b.buildingName || '',
      address: b.address || '',
      adminName: b.adminName || '',
      adminPhone: b.adminPhone || '',
      description: b.description || '',
      phone: b.phone || '',
      parkingAvailable: b.parkingAvailable || false,
    }
  } catch {
    message.error('건물 정보를 불러오는데 실패했습니다.')
  }
}

async function handleSave() {
  if (!form.value.buildingName) {
    message.warning('건물 이름을 입력해주세요.')
    return
  }
  saving.value = true
  try {
    await api.put(`/buildings/${buildingId}`, {
      ...form.value,
      floors: [],
    })
    message.success('건물 정보가 수정되었습니다.')
  } catch {
    message.error('수정에 실패했습니다.')
  } finally {
    saving.value = false
  }
}

async function handleDelete() {
  try {
    await api.delete(`/buildings/${buildingId}`)
    message.success('건물이 삭제되었습니다.')
    router.push('/')
  } catch {
    message.error('삭제에 실패했습니다.')
  }
}

onMounted(fetchBuilding)
</script>

<template>
  <NLayout>
    <NLayoutHeader bordered style="padding: 12px 24px; display: flex; align-items: center; gap: 16px">
      <NButton @click="router.push(`/building/${buildingId}`)">← 메뉴</NButton>
      <h2 style="margin: 0">건물 정보 수정</h2>
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
        <NSpace justify="space-between" style="margin-top: 24px">
          <NPopconfirm @positive-click="handleDelete">
            <template #trigger>
              <NButton type="error">건물 삭제</NButton>
            </template>
            정말 이 건물을 삭제하시겠습니까? 모든 방 정보도 함께 삭제됩니다.
          </NPopconfirm>
          <NButton type="primary" :loading="saving" @click="handleSave">저장</NButton>
        </NSpace>
      </NCard>
    </NLayoutContent>
  </NLayout>
</template>
