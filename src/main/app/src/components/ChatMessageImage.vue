<script setup lang="ts">
import type { PropType } from 'vue';


const props = defineProps({
    content: {
        type: Object as PropType<string>,
        required: true
    }
})

</script>
<template>
    <img class="chat-message-img" :src="props.content">
</template>