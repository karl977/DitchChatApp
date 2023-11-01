<script setup lang="ts">
    import { ref } from 'vue'
    import { usePrimeVue } from 'primevue/config';
    import {useDarkmodeStore} from "@/stores/darkmode"

    const PrimeVue = usePrimeVue();
    const DarkmodeStore = useDarkmodeStore();

    function toggleDarkMode(){
        if(DarkmodeStore.darkmode){
            PrimeVue.changeTheme('bootstrap4-dark-purple', 'bootstrap4-light-purple', 'theme-link', () => {});
            document.getElementsByTagName("html")[0].setAttribute("data-darkmode", 'false');
        }else{
            PrimeVue.changeTheme('bootstrap4-light-purple', 'bootstrap4-dark-purple',  'theme-link', () => {});
            document.getElementsByTagName("html")[0].setAttribute("data-darkmode", 'true');
        }
        DarkmodeStore.toggle();
    }

</script>
<template>
    <Button @click="toggleDarkMode">
        <i v-if="DarkmodeStore.darkmode" class="pi pi-moon"></i>
        <i v-if="!DarkmodeStore.darkmode" class="pi pi-sun"></i>
    </Button>
</template>