<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, NCard, NForm, NFormItem, NInput, NButton, NSpace } from 'naive-ui'
import api from '@/api/axios'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()

const loginId = ref('')
const password = ref('')
const loading = ref(false)

async function handleLogin() {
  if (!loginId.value || !password.value) {
    message.warning('아이디와 비밀번호를 입력해주세요.')
    return
  }
  loading.value = true
  try {
    const res = await api.post('/auth/login', {
      loginId: loginId.value,
      password: password.value,
    })
    const { accessToken, refreshToken } = res.data.data
    authStore.setTokens(accessToken, refreshToken)
    message.success('로그인 성공')
    router.push('/')
  } catch (err: any) {
    message.error(err.response?.data?.message || '로그인에 실패했습니다.')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <NCard title="에셋마스터 로그인" style="width: 400px">
      <NForm>
        <NFormItem label="아이디">
          <NInput v-model:value="loginId" placeholder="아이디 입력" @keyup.enter="handleLogin" />
        </NFormItem>
        <NFormItem label="비밀번호">
          <NInput v-model:value="password" type="password" show-password-on="click" placeholder="비밀번호 입력" @keyup.enter="handleLogin" />
        </NFormItem>
        <NSpace vertical :size="12" style="width: 100%">
          <NButton type="primary" block :loading="loading" @click="handleLogin">로그인</NButton>
          <NButton block @click="router.push('/signup')">회원가입</NButton>
        </NSpace>
      </NForm>
    </NCard>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}
</style>
