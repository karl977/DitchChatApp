<script setup lang="ts">
import InputText from 'primevue/inputtext'
import { ref, type Ref } from 'vue'
import { RouterLink } from 'vue-router';
import DarkModeToggle from '@/components/DarkModeToggle.vue';
import axios from '@/axios';
import router from '@/router';

const username = ref("")
const password = ref("");
const color = ref("ff3c00");
const loading = ref(false);
const errorMessage: Ref<string | null> = ref(null);

function register() {
    
    errorMessage.value = null;

    if (username.value.length === 0) {
        errorMessage.value = "Username is required"
        return;
    }
    if (password.value.length === 0) {
        errorMessage.value = "Password is required"
        return;
    }
    
    loading.value = true;

    axios.post('/api/register', {
        username: username.value,
        password: password.value,
        color: color.value
    })
        .then(function (response) {
            if(response.data.success){
                localStorage.setItem("registrationSuccess", "true")
                localStorage.setItem("username", username.value)
                router.push("/login")
            }else{
                errorMessage.value = "Unknown error occurred"
            }
        })
        .catch(function (error) {
            if (error.response?.data?.error) {
                errorMessage.value = error.response.data.error;
            }else{
                errorMessage.value = "Unknown error occurred"
            }
        }).finally(function(){
            loading.value = false;
        });
}

</script>
<template>
    <div class="flex flex-column flex-nowrap w-full h-full align-items-center justify-content-center">
        <img class="mb-7" style="width:300px" src="@/assets/ditch.png">
        <div class="login_card surface-card border-round-xl">
            <div class="flex flex-column align-content-center justify-content-center px-8">
                <div class="text-6xl mb-2">Register</div>
                <div class="text-500 field">Register an account to continue</div>
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
                    <Password v-model="password" placeholder="Password" toggleMask />
                </div>
                <div class="field text-center">
                    <span class="text-700">Choose chat username color:</span>
                    <ColorPicker v-model="color" class="mx-2" />
                </div>
                <Message v-if="errorMessage != null" class="mt-0" severity="error" :closable="false">{{ errorMessage }}
                </Message>
                <ProgressBar v-if="loading" class="field" mode="indeterminate" style="height: 6px"></ProgressBar>
                <div class="field flex align-content-center justify-content-center">
                    <Button @click="register">Register</Button>
                </div>
                <div class="text-center field">
                    <span class="text-700">Already registered? </span>
                    <RouterLink to="login">Sign in here</RouterLink>
                </div>
                <div class="text-center field">
                    <DarkModeToggle />
                </div>
            </div>
        </div>
</div></template>