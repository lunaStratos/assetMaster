<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NLayout, NLayoutHeader, NLayoutContent,
  NCard, NButton, NSpace, NGrid, NGi, NTag, NDrawer, NDrawerContent,
  NForm, NFormItem, NInput, NInputNumber, NDatePicker, NSelect, NUpload,
  NDivider, NDescriptions, NDescriptionsItem,
  useMessage,
} from 'naive-ui'
import type { UploadFileInfo } from 'naive-ui'
import api from '@/api/axios'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const buildingId = route.params.id as string

interface Room {
  id: number
  buildingId: number
  floor: number
  roomNumber: string
  status: string
  tenantName: string | null
  tenantPhone: string | null
  country: string | null
  deposit: number | null
  monthlyRent: number | null
  accountNumber: string | null
  startDate: string | null
  endDate: string | null
  description: string | null
  leaseFilePath: string | null
}

interface Building {
  id: number
  buildingName: string
  address: string
}

const building = ref<Building | null>(null)
const rooms = ref<Room[]>([])
const loading = ref(false)
const drawerVisible = ref(false)
const selectedRoom = ref<Room | null>(null)
const editForm = ref<any>({})
const saving = ref(false)

const statusOptions = [
  { label: '공실', value: 'VACANT' },
  { label: '점유', value: 'OCCUPIED' },
  { label: '수리중', value: 'MAINTENANCE' },
]

const floorGroups = computed(() => {
  const groups: Record<number, Room[]> = {}
  rooms.value.forEach((r) => {
    if (!groups[r.floor]) groups[r.floor] = []
    groups[r.floor]!.push(r)
  })
  return Object.entries(groups)
    .sort(([a], [b]) => Number(b) - Number(a))
    .map(([floor, roomList]) => ({ floor: Number(floor), rooms: roomList }))
})

function statusColor(status: string) {
  switch (status) {
    case 'OCCUPIED': return 'success'
    case 'MAINTENANCE': return 'warning'
    default: return 'default'
  }
}

function statusLabel(status: string) {
  switch (status) {
    case 'OCCUPIED': return '점유'
    case 'MAINTENANCE': return '수리중'
    default: return '공실'
  }
}

function openDrawer(room: Room) {
  selectedRoom.value = room
  editForm.value = {
    roomNumber: room.roomNumber,
    status: room.status,
    tenantName: room.tenantName || '',
    tenantPhone: room.tenantPhone || '',
    country: room.country || '',
    deposit: room.deposit,
    monthlyRent: room.monthlyRent,
    accountNumber: room.accountNumber || '',
    startDate: room.startDate ? new Date(room.startDate).getTime() : null,
    endDate: room.endDate ? new Date(room.endDate).getTime() : null,
    description: room.description || '',
  }
  drawerVisible.value = true
}

async function fetchData() {
  loading.value = true
  try {
    const [bRes, rRes] = await Promise.all([
      api.get(`/buildings/${buildingId}`),
      api.get(`/rooms/building/${buildingId}`),
    ])
    building.value = bRes.data.data
    rooms.value = rRes.data.data
  } catch {
    message.error('데이터를 불러오는데 실패했습니다.')
  } finally {
    loading.value = false
  }
}

async function saveRoom() {
  if (!selectedRoom.value) return
  saving.value = true
  try {
    const payload = {
      ...editForm.value,
      startDate: editForm.value.startDate ? new Date(editForm.value.startDate).toISOString().split('T')[0] : null,
      endDate: editForm.value.endDate ? new Date(editForm.value.endDate).toISOString().split('T')[0] : null,
    }
    await api.put(`/rooms/${selectedRoom.value.id}`, payload)
    message.success('저장되었습니다.')
    drawerVisible.value = false
    fetchData()
  } catch {
    message.error('저장에 실패했습니다.')
  } finally {
    saving.value = false
  }
}

async function handleUpload({ file }: { file: UploadFileInfo }) {
  if (!selectedRoom.value || !file.file) return
  const formData = new FormData()
  formData.append('file', file.file)
  try {
    await api.post(`/rooms/${selectedRoom.value.id}/lease-file`, formData)
    message.success('파일이 업로드되었습니다.')
    fetchData()
  } catch {
    message.error('업로드에 실패했습니다.')
  }
}

onMounted(fetchData)
</script>

<template>
  <NLayout>
    <NLayoutHeader bordered style="padding: 12px 24px; display: flex; align-items: center; gap: 16px">
      <NButton @click="router.push(`/building/${buildingId}`)">← 메뉴</NButton>
      <h2 style="margin: 0">{{ building?.buildingName || '...' }} - 방 관리</h2>
    </NLayoutHeader>
    <NLayoutContent style="padding: 24px">
      <div v-for="fg in floorGroups" :key="fg.floor" style="margin-bottom: 24px">
        <h3 style="margin-bottom: 8px">{{ fg.floor }}층</h3>
        <NGrid :x-gap="12" :y-gap="12" cols="2 400:3 600:4 900:6">
          <NGi v-for="room in fg.rooms" :key="room.id">
            <NCard
              size="small"
              hoverable
              style="cursor: pointer; text-align: center"
              :style="{
                borderLeft: room.status === 'OCCUPIED' ? '4px solid #18a058' :
                             room.status === 'MAINTENANCE' ? '4px solid #f0a020' : '4px solid #d3d3d3'
              }"
              @click="openDrawer(room)"
            >
              <div style="font-weight: bold; font-size: 16px">{{ room.roomNumber }}</div>
              <NTag :type="statusColor(room.status)" size="small">{{ statusLabel(room.status) }}</NTag>
              <div v-if="room.tenantName" style="margin-top: 4px; font-size: 12px; color: #666">
                {{ room.tenantName }}
              </div>
            </NCard>
          </NGi>
        </NGrid>
      </div>
    </NLayoutContent>

    <NDrawer v-model:show="drawerVisible" :width="450" placement="right">
      <NDrawerContent :title="`${selectedRoom?.roomNumber}호 상세`" closable>
        <NForm label-placement="left" label-width="100">
          <NFormItem label="호수">
            <NInput v-model:value="editForm.roomNumber" />
          </NFormItem>
          <NFormItem label="상태">
            <NSelect v-model:value="editForm.status" :options="statusOptions" />
          </NFormItem>
          <NDivider>세입자 정보</NDivider>
          <NFormItem label="세입자명">
            <NInput v-model:value="editForm.tenantName" placeholder="세입자명" />
          </NFormItem>
          <NFormItem label="전화번호">
            <NInput v-model:value="editForm.tenantPhone" placeholder="전화번호" />
          </NFormItem>
          <NFormItem label="국가">
            <NInput v-model:value="editForm.country" placeholder="국가" />
          </NFormItem>
          <NDivider>계약 정보</NDivider>
          <NFormItem label="보증금">
            <NInputNumber v-model:value="editForm.deposit" :show-button="false" style="width: 100%">
              <template #suffix>원</template>
            </NInputNumber>
          </NFormItem>
          <NFormItem label="월세">
            <NInputNumber v-model:value="editForm.monthlyRent" :show-button="false" style="width: 100%">
              <template #suffix>원</template>
            </NInputNumber>
          </NFormItem>
          <NFormItem label="계좌번호">
            <NInput v-model:value="editForm.accountNumber" placeholder="계좌번호" />
          </NFormItem>
          <NFormItem label="계약 시작일">
            <NDatePicker v-model:value="editForm.startDate" type="date" style="width: 100%" />
          </NFormItem>
          <NFormItem label="계약 종료일">
            <NDatePicker v-model:value="editForm.endDate" type="date" style="width: 100%" />
          </NFormItem>
          <NFormItem label="설명">
            <NInput v-model:value="editForm.description" type="textarea" />
          </NFormItem>
          <NDivider>임대차계약서</NDivider>
          <NFormItem label="파일 업로드">
            <NUpload :custom-request="() => {}" @change="handleUpload" :max="1" accept=".pdf,.jpg,.jpeg,.png">
              <NButton>파일 선택</NButton>
            </NUpload>
          </NFormItem>
          <div v-if="selectedRoom?.leaseFilePath" style="margin-bottom: 16px; color: #18a058">
            현재 파일: {{ selectedRoom.leaseFilePath }}
          </div>
        </NForm>
        <template #footer>
          <NSpace justify="end">
            <NButton @click="drawerVisible = false">취소</NButton>
            <NButton type="primary" :loading="saving" @click="saveRoom">저장</NButton>
          </NSpace>
        </template>
      </NDrawerContent>
    </NDrawer>
  </NLayout>
</template>
