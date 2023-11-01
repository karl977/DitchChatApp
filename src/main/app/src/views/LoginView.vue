<script setup lang="ts">
import { ref, type Ref, onMounted } from 'vue'
import { RouterLink } from 'vue-router';
import DarkModeToggle from '@/components/DarkModeToggle.vue';
import axios from '@/axios';
import router from '@/router';
import { useToast } from "primevue/usetoast";

const toast = useToast();

const username = ref(localStorage.getItem("username") ?? "");
const password = ref("");
const loading = ref(false);

const errorMessage : Ref<string|null> = ref(null);

function login() {

    errorMessage.value = null;
    localStorage.setItem("username", username.value);

    let formData = new FormData();
    formData.append("username", username.value);
    formData.append("password", password.value);
    
    loading.value = true;

    axios({
        method: "POST",
        url: "/api/login_process",
        data: formData,
    }).then(response => {
        router.push('/');
    }).catch(error => {
        if(error.response.status === 401){
            errorMessage.value = "Invalid username or password";
        }else{
            errorMessage.value = "Unknown error occurred";
        }
    }).finally(() => {
        loading.value = false;
    });
}

onMounted(() => {
    if(localStorage.getItem("registrationSuccess") === "true"){
        toast.add({ severity: 'success', summary: 'Success', detail: 'Registration successful', life: 5000 });
        localStorage.removeItem("registrationSuccess")
    }
})

</script>

<template>
    <Toast/>
    <div class="flex flex-column flex-nowrap w-full h-full align-items-center justify-content-center">
        <img class="mb-7" style="width:300px" src="@/assets/ditch.png">
        <div class="login_card surface-card border-round-xl">
            <div class="flex flex-column align-content-center justify-content-center px-8">
                <div class="text-6xl mb-2">Login</div>
                <div class="text-500 field">Sign in to you account to continue</div>
                <div class="p-inputgroup flex-1 field">
                    <span class="p-inputgroup-addon">
                        <i class="pi pi-user"></i>
                    </span>
                    <InputText v-model="username" placeholder="Username" />
                </div>
                <div class="p-inputgroup flex-1 field">
                    <span class="p-inputgroup-addon">
                        <i class="pi pi-lock"></i>
                    </span>
                    <Password v-model="password" v-on:keyup.enter="login" :feedback="false" placeholder="Password" toggleMask />
                </div>
                <Message v-if="errorMessage != null" class="mt-0" severity="error" :closable="false">{{ errorMessage }}
                </Message>
                <ProgressBar v-if="loading" class="field" mode="indeterminate" style="height: 6px"></ProgressBar>
                <div class="field flex align-content-center justify-content-center">
                    <Button @click="login">Log in</Button>
                </div>
                <div class="text-center field">
                    <span class="text-700">Not registered? </span>
                    <RouterLink to="register">Create a new account</RouterLink>
                </div>
                <div class="text-center field">
                    <DarkModeToggle></DarkModeToggle>
                </div>
            </div>
        </div>
    </div>
</template>
