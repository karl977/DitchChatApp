<script setup lang="ts">
import type { ChatMessageType } from '@/types';
import { type PropType, onMounted, ref } from 'vue';

const show = ref(false);

onMounted(() => {
    setTimeout(() => {
        show.value = true
    }, 200);
})

const props = defineProps({
    content: {
        type: Object as PropType<ChatMessageType>,
        required: true
    }
})

</script>
<template>
    <div :style="{ display: show ? 'block' : 'none' }">
        <div class="inline-flex align-content-center justify-content-center vertical-align-middle badge-img-container">
            <img v-for="badgeUrl in props.content.badges" class="badge-img" :src="badgeUrl">
        </div>
        <span class="font-bold vertical-align-middle" :style="{ color: props.content.color }">
            {{ props.content.username }}
        </span>
        <span class="mr-1 vertical-align-middle">:</span>
        <component v-for="content in props.content.messageContents" :is="content.type == 'img' ? 'div' : 'span'"
            :class="content.type == 'img' ? 'chat-message-img-container' : 'chat-message-text-container'">
            {{ content.type == "span" ? content.text : "" }}
            <img v-if="content.type == 'img'" :src="content.text">
        </component>


    </div>
</template>