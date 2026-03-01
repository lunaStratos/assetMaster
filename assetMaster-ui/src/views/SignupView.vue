<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, NCard, NForm, NFormItem, NInput, NButton, NSpace } from 'naive-ui'
import api from '@/api/axios'

const router = useRouter()
const message = useMessage()

const form = ref({
  loginId: '',
  password: '',
  passwordConfirm: '',
  name: '',
  phone: '',
})
const loading = ref(false)

async function handleSignup() {
  const f = form.value
  if (!f.loginId || !f.password || !f.name || !f.phone) {
    message.warning('모든 항목을 입력해주세요.')
    return
  }
  if (f.password !== f.passwordConfirm) {
    message.warning('비밀번호가 일치하지 않습니다.')
    return
  }
  loading.value = true
  try {
    await api.post('/auth/signup', {
      loginId: f.loginId,
      password: f.password,
      name: f.name,
      phone: f.phone,
    })
    message.success('회원가입이 완료되었습니다.')
    router.push('/login')
  } catch (err: any) {
    message.error(err.response?.data?.message || '회원가입에 실패했습니다.')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="signup-container">
    <NCard title="회원가입" style="width: 400px">
      <NForm>
        <NFormItem label="아이디">
          <NInput v-model:value="form.loginId" placeholder="아이디 입력" />
        </NFormItem>
        <NFormItem label="비밀번호">
          <NInput v-model:value="form.password" type="password" show-password-on="click" placeholder="비밀번호 입력" />
        </NFormItem>
        <NFormItem label="비밀번호 확인">
          <NInput v-model:value="form.passwordConfirm" type="password" show-password-on="click" placeholder="비밀번호 확인" />
        </NFormItem>
        <NFormItem label="이름">
          <NInput v-model:value="form.name" placeholder="이름 입력" />
        </NFormItem>
        <NFormItem label="전화번호">
          <NInput v-model:value="form.phone" placeholder="010-0000-0000" />
        </NFormItem>
        <NSpace vertical :size="12" style="width: 100%">
          <NButton type="primary" block :loading="loading" @click="handleSignup">회원가입</NButton>
          <NButton block @click="router.push('/login')">로그인으로 돌아가기</NButton>
        </NSpace>
      </NForm>
    </NCard>
  </div>
</template>

<style scoped>
.signup-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}
</style>
