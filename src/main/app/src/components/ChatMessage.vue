<script setup lang="ts">
import type { ChatMessageType } from '@/types';
import { type PropType, onMounted, ref } from 'vue';

const show = ref(false);

onMounted(() => {
    setTimeout(() => {
        show.value = true
    }, 100);
})

const props = defineProps({
    content: {
        type: Object as PropType<ChatMessageType>,
        required: true
    }
})

</script>
<template>
    <div :style="{display: show ? 'block' : 'none'}">
        <img v-for="badgeUrl in props.content.badges" class="badge-img" :src="badgeUrl">
        <span class="font-bold" :style="{ color: props.content.color }">{{ props.content.username }}</span>
        <span class="mr-1">:</span>
        <component :is="content.type" :class="content.type == 'img' ? 'chat-message-img' : null"
            v-for="content in props.content.messageContents" :src="content.type == 'img' ? content.text : null">{{
                        content.type =="span" ? content.text : "" }}</component>
    </div>
</template>