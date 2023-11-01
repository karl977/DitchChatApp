import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useDarkmodeStore = defineStore('darkmode', () => {
  let darkmode = ref(localStorage.getItem("darkmode") !== "false")
  
  function toggle(){
    darkmode.value = !darkmode.value;
    localStorage.setItem("darkmode", darkmode.value ? "true" : "false");
  }

  return {darkmode, toggle}
})
