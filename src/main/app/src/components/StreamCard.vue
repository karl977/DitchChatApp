<script setup lang="ts">
import type { Stream } from '@/types';
import type { PropType } from 'vue';
import { ref, computed } from 'vue'
import router from '@/router';

const zoom = ref('');

const props = defineProps({
    stream: {
        type: Object as PropType<Stream>,
        required: true
    }
})

function zoomIn() {
    zoom.value = "card-zoom"
}

function zoomOut() {
    zoom.value = ""
}

const viewerCount = computed(() => {
    if(props.stream.viewers >= 1000){
        let k = props.stream.viewers/1000
        let rounded = Math.round(k * 10) / 10
        return rounded.toFixed(1) + "K";
    }else{
        return props.stream.viewers;
    }
})

function open(){
    router.push("/stream/" + props.stream.nid)
}

</script>
<template>
    <div class="border-round-lg stream-card" @click="open" style="height:325px; width: 440px; cursor: pointer;">
        <div class="stream-card-image-container overflow-hidden" style="position:absolute">
            <div :class="zoom" class="stream-card-image-holder "
                v-bind:style="{ backgroundImage: 'url(' + props.stream.image_url + ')' }"></div>
        </div>
        <div style="position:absolute" @mouseover="zoomIn" @mouseout="zoomOut">
            <div class="flex flex-column justify-content-between align-items-start text-white" style="height:248px; width: 440px;">
                <span class="bg-red-500 m-2" style="padding:2px 8px;">Live</span>
                <span class="m-2 viewers-badge border-round-lg px-2 py-1">
                    <i class="pi pi-user" />
                    {{ viewerCount }}
                </span>
            </div>
            <div class="flex flex-row">
                <Avatar :image="`${props.stream.avatar_url}`" class="m-2" size="large" shape="circle" style="margin-top: 10px;">
                </Avatar>
                <div class="flex flex-column p-2">
                    <span class="font-bold">{{ props.stream.title }}</span>
                    <span style="font-size: 0.95rem;">{{ props.stream.name }}</span>
                    <span style="font-size: 0.95rem;">{{ props.stream.category }}</span>
                </div>
            </div>
        </div>
    </diV>
</template>