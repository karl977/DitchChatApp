<script setup lang="ts">
import { RouterView } from 'vue-router'
import { ref, onBeforeMount } from 'vue'
import { usePrimeVue } from 'primevue/config';
import '/node_modules/primeflex/primeflex.css'
import { useDarkmodeStore } from "@/stores/darkmode"
import Stomp from "webstomp-client";


const PrimeVue = usePrimeVue();

const Darkmodestore = useDarkmodeStore();

onBeforeMount(() => {
  if (!Darkmodestore.darkmode) {
    PrimeVue.changeTheme('bootstrap4-dark-purple', 'bootstrap4-light-purple', 'theme-link', () => { });
    document.getElementsByTagName("html")[0].setAttribute("data-darkmode", 'false');
  }
})


// let stompClient = Stomp.client("ws://localhost/websocket")
// stompClient.connect(
//   {},
//   frame => {
//     console.log(frame);
//     stompClient.subscribe("/topic/greetings", tick => {
//       console.log(tick);
//     });
//     stompClient.unsubscribe
//   },
//   error => {
//     console.log(error);
//   }
// );

// function send() {
//   stompClient.send("/app/hello", JSON.stringify({ 'name': "Karl" }))
// }
</script>

<template>
  <RouterView />
</template>
<style lang="scss">
@import url('./assets/style.scss');
</style>