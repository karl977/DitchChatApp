<script setup lang="ts">
import ChatMessage from '@/components/ChatMessage.vue';
import type { ChatMessageType, Stream } from '@/types';
import axios from '@/axios';
import { ref, type Ref, type PropType, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import Stomp from "webstomp-client";
import { getCookie } from "@/util"

const router = useRoute();
const id = router.params.id as string;

let displayEnterMessage = ref(true);
let messageEnterBox: Ref<HTMLInputElement | null> = ref(null)
let chatBox: Ref<HTMLElement | null> = ref(null)
let messages: Ref<ChatMessageType[]> = ref([]);
let stream: Ref<Stream | null> = ref(null);

let stickToBottom = true;

let stompClient = Stomp.client("ws://" + location.host + "/websocket", { debug: true })
stompClient.debug = function () { }


onMounted(() => {
    chatBox.value?.addEventListener("scroll", chatScroll)
    axios("/api/user/streams/" + id)
        .then(response => {
            stream.value = <Stream>response.data;
        }).catch(error => {
            console.log(error);
        })

    connectStomp({ "X-XSRF-TOKEN": getCookie("XSRF-TOKEN") })
})

onUnmounted(() => {
    chatBox.value?.removeEventListener("scroll", chatScroll)
    stompClient.unsubscribe("/topic/stream/" + id);
    stompClient.disconnect();
})

function chatScroll(event: Event) {
    if (chatBox.value) {
        let cb = chatBox.value;
        stickToBottom = (cb.scrollHeight - (cb.scrollTop + cb.clientHeight) < 10)
    }
}

function scrollChatToBottom() {
    nextTick(() => {
        if (stickToBottom) {
            setTimeout(() => {
                if (chatBox.value) {
                    chatBox.value.scrollTop = chatBox.value.scrollHeight;
                }
            }, 100)
        }
    })
}

function onKeyUp() {
    displayEnterMessage.value = !messageEnterBox.value?.innerText
}

function onEnterPress(event: Event) {
    event.preventDefault()
    if (messageEnterBox.value?.innerText) {
        sendMessage(messageEnterBox.value?.innerText)
        messageEnterBox.value.innerText = "";
    }
}

function onChatButtonPress() {
    if (messageEnterBox.value?.innerText) {
        sendMessage(messageEnterBox.value?.innerText)
        messageEnterBox.value.innerText = "";
    }
}

function connectStomp(headers: any) {
    stompClient.connect(
        headers,
        frame => {
            stompClient.subscribe("/topic/stream/" + id, tick => {
                let message = <ChatMessageType>JSON.parse(tick.body);
                if (messages.value.length > 200) {
                    messages.value.shift();
                }
                messages.value.push(message);
                scrollChatToBottom()
            });
        },
        error => {
            console.log(error);
        }
    );
}

function sendMessage(message: string) {
    stompClient.send("/app/stream/" + id, JSON.stringify({ 'text': message }))
}

</script>
<template>
    <div class="content flex flex-row">
        <div class="flex-grow-1">
            <iframe v-if="stream" class="w-full clip-view"
                :src="`https://clips.twitch.tv/embed?clip=${stream.video_id}&parent=localhost`" frameborder="0"
                allowfullscreen="true" scrolling="no"></iframe>
        </div>
        <div class="flex flex-column chat-container">
            <div class="text-center font-bold chat-header">Chat</div>
            <div ref="chatBox" class="px-2 flex-grow-1 chat-messages-container">
                <ChatMessage class="chat-message" v-for="message in messages" :content="message" :key="message.id" />
            </div>
            <div class="message-box-container">
                <div :style="{ display: displayEnterMessage ? 'block' : 'none' }" class="send-message-text">Send a message
                </div>
                <div @keypress.enter="onEnterPress" @keyup="onKeyUp" ref="messageEnterBox" class="message-box"
                    contenteditable="true"></div>
            </div>
            <div class="chat-footer">
                <div class="channel-points-container my-1">
                    <svg class="channel-points-vector" width="20" height="20" viewBox="0 0 20 20">
                        <path d="M10 6a4 4 0 0 1 4 4h-2a2 2 0 0 0-2-2V6z"></path>
                        <path fill-rule="evenodd"
                            d="M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0zm-2 0a6 6 0 1 1-12 0 6 6 0 0 1 12 0z"
                            clip-rule="evenodd"></path>
                    </svg>
                    <span>20.2K</span>
                </div>
                <Button @click="onChatButtonPress" class="chat-button">Chat</Button>
            </div>
        </div>
    </div>
</template>