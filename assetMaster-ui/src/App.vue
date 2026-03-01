<script setup lang="ts">
import { NConfigProvider, NMessageProvider, koKR, dateKoKR } from 'naive-ui'
</script>

<template>
  <NConfigProvider :locale="koKR" :date-locale="dateKoKR">
    <NMessageProvider>
      <router-view />
    </NMessageProvider>
  </NConfigProvider>
</template>
