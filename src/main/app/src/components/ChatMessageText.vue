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
    <span>{{props.content}}</span>
</template>