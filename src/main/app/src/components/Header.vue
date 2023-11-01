<script setup lang="ts">

import router from '@/router';
import axios from 'axios'
import DarkModeToggle from './DarkModeToggle.vue';

function logout() {
  axios.post("/api/logout")
    .then(response => {
      router.push('/login')
    });
}

function settings(){
  router.push("/settings")
}

function home(){
  router.push("/")
}

</script>
<template>
  <header>
    <div class="w-full" style="height:80px; padding:0px 12rem">
      <img src="@/assets/ditch.png" @click="home" class="mt-2" style="height:40px; float:left; cursor: pointer;">
      <!-- <nav style="float:left; padding: 15px 50px;">
          <RouterLink style="float:left;" to="/">Home</RouterLink>
          <RouterLink to="/settings"  style="float:left;">Settings</RouterLink>
        </nav> -->
      <div class="header-buttons" style="float:right;">
        <!-- <Button @click="settings"><i class="pi pi-wrench" /></Button> -->
        <DarkModeToggle />
        <Button @click="logout"><i class="pi pi-sign-out" /></Button>
      </div>
    </div>
  </header>
</template>