<template>
  <div 
  v-if="modalStore.isOpen('change-password-form')"
  class="change-password-container">
    <ChangePasswordFormView />
  </div>
  <div
  v-if="modalStore.isOpen('change-email-form')"
  class="change-email-container">
    <ChangeFormView
      :icon="codeIcon"
      title="Введите email"
      desc="Введите адрес электронной почты, чтобы иметь возможность восстановить доступ к аккаунту. 
      Для подтверждения, вам будет отправлен код на вашу почту"
      button-name="Отправить код"
      v-model="newEmail"
      input-placeholder="example@example.com"
      input-type="email"
      :submit-func="submitEmailRequest"
    />
  </div>
  <div
  v-if="modalStore.isOpen('confirm-code-form')"
  class="confirm-code-container">
    <ChangeFormView
      :icon="emailIcon"
      title="Введите код подтверждения"
      desc="Введите код, чтобы привязать новый адрес электронной почты к вашему аккаунту. 
      Для подтверждения, введите этот код снизу. Код был отправлен на указанный вами email"
      button-name="Подтвердить"
      v-model="codeValue"
      input-placeholder="123456"
      input-type="text"
      :submit-func="submitCodeConfirm"
    />
  </div>
  <div class="main-container">
    <div class="user-info-wrapper">
      <div class="user-info-container">
        <div class="title">
          ИНФОРМАЦИЯ
        </div>
        <div class="info-block">
          <span>
            <img src="../../../assets/icons/user/id.svg" alt="id-img">
          </span>
          ID: {{ userInfo?.id }}
        </div>
        <div class="info-block">
          <span>
            <img src="../../../assets/icons/user/name.svg" alt="">
          </span>
          Имя пользователя: {{ userInfo?.username }}
        </div>
        <template v-if="userInfo?.email !== null">
          <div class="info-block">
          <span>
            <img src="../../../assets/icons/user/email.svg" alt="id-img">
          </span>
            Email: {{ userInfo?.email }}
          </div>
        </template>
        <template v-else>
          <div class="info-block">
            <span>
              <img src="../../../assets/icons/user/email.svg" alt="id-img">
            </span>
            Email: Отсутствует
          </div>
        </template>
        <div class="button-block">
          <button @click.stop="modalStore.open('change-email-form')">
            Изменить email
          </button>
        </div>
        <div class="info-block password-block">
          <span>
            <img src="../../../assets/icons/user/passkey.svg" alt="id-img">
          </span>
          Пароль: <input disabled type="password" value="12345678">
        </div>
        <div class="button-block">
          <button @click.stop="modalStore.open('change-password-form')">
            Изменить пароль
          </button>
        </div>
        <div class="info-container">
      <div>
        <h2 class="subtitle-position1">
          Текущая сессия
        </h2>
      </div>
      <div
        v-for="device in props.userInfo?.devices"
        :key="device.id"
      >
        <div
          v-if="device.id === deviceId"
          class="useragent-container"
        >
          {{ device.userAgent }}
        </div>
      </div>
      <div>
        <h2 class="subtitle-position2">
          Сессии
        </h2>
      </div>
      <div 
        v-for="device in props.userInfo?.devices"
        :key="device.id" 
        class="useragent-container"
      >
        <div 
          class="device-item"
        >
          {{ device.userAgent }}
        </div>
        <div class="delete-item">
          <button 
            @click="deleteSession(device.id)"
          >
            Удалить
          </button>
        </div>
      </div>
    </div>
      </div>  
    </div>
  </div>
</template>
<script setup lang="ts">
import emailIcon from "../../../assets/icons/mail.svg";
import codeIcon from "../../../assets/icons/lock.svg";
import ChangeFormView from '@/components/atom/ChangeFormView.vue';
import ChangePasswordFormView from '@/components/atom/ChangePasswordFormView.vue';
import { authorizationService } from '@/services/authorizationService';
import { userService } from '@/services/userService';
import { useModalStore } from '@/stores/useModalStore';
import { UserInfo } from '@/types/userData';
import { ref } from 'vue';

import { AxiosError } from 'axios';
import notification from '@/plugins/notifications';

const props = defineProps<{
  userInfo: UserInfo | null;
}>();

const modalStore = useModalStore();
const deviceId = ref();

const newEmail = ref<string>("");
const codeValue = ref<string>("");

const submitEmailRequest = async(): Promise<void> => {
  if(!newEmail.value || !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(newEmail.value)) {
    notification.error("Введите email!");
    return;
  }
  try{
    await userService.requestUserEmail(newEmail.value);
    notification.success("Код с подтверждением отправлен вам на почту!");
    newEmail.value = "";
    modalStore.close('change-email-form');
    modalStore.open('confirm-code-form')
  }
  catch(e){
    const axiosError = e as AxiosError;
    console.log(axiosError.status);
  }
}

const submitCodeConfirm = async(): Promise<void> => {
  if(!codeValue.value || codeValue.value.length < 5){
    notification.error("Введите действительный код!");
    return;
  }
  try{
    await userService.confirmUserEmail(codeValue.value);
    notification.success("Новая почта была успешно привязана к вашему аккаунту!");
    codeValue.value = '';
    modalStore.close('confirm-code-form');
  }
  catch(e){
    const axiosError = e as AxiosError;
    console.log(axiosError);
  }
}

const deleteSession = async(id: number) => {
  try{
    if(deviceId.value === id) {
      await userService.deleteSession(id);
      await authorizationService.logoutUser();
    }
  }
  catch(e){
    console.error(e)
  }
}
</script>
<style scoped lang="sass">
@import '@/assets/styles/molecule/profile/user-setting.sass'
</style>