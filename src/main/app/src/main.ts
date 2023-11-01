import 'primeicons/primeicons.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import Password from "primevue/password";
import ColorPicker from 'primevue/colorpicker';
import Message from 'primevue/message';
import ProgressSpinner from 'primevue/progressspinner';
import ProgressBar from 'primevue/progressbar';
import Toast from 'primevue/toast';
import Card from 'primevue/card';
import Avatar from 'primevue/avatar';

const app = createApp(App)

app.component('Button', Button)
app.component('InputText', InputText)
app.component('Password', Password)
app.component('ColorPicker', ColorPicker)
app.component('Message', Message)
app.component('ProgressSpinner', ProgressSpinner)
app.component('ProgressBar', ProgressBar)
app.component('Toast', Toast)
app.component('Card', Card)
app.component('Avatar', Avatar)


app.use(createPinia())
app.use(router)
app.use(PrimeVue)
app.use(ToastService);

app.mount('#app')
