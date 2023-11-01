<script setup lang="ts">
import StreamCard from '@/components/StreamCard.vue';
import type { Stream } from '@/types';
import axios from '@/axios';
import { onMounted, ref, type Ref } from 'vue';

let loading = ref(true);
let streams: Ref<Stream[]> = ref([]);

onMounted(() => {
  axios("/api/user/streams")
    .then(response => {
      streams.value = <Stream[]> response.data;
    }).catch(error => {
      console.log(error);
    })
})

</script>
<template>
  <div class="content">
    <h1>Following</h1>
    <div class="flex flex-row streams-container flex-wrap">
      <StreamCard v-for="stream in streams" :stream="stream"></StreamCard>
    </div>
  </div>
</template>
