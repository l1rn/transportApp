<template>
  <div class="form-wrapper">
    <div
    v-if="modalStore.isOpen('change-password-form')" 
    class="change-password-container">
      <ChangePasswordFormView 
        
      />
    </div>
    <div v-if="activeModalKey"
    class="change-modal-container">
      <ModalFormView
        v-bind="activeModal!"
        v-model="activeModal!.model.value"
      />
    </div>
  </div>
  <div class="main-container">
    <div class="user-info-wrapper">
      <div class="user-info-container">
        <div class="title">
          ИНФОРМАЦИЯ
        </div>
        <div class="main-container">
          <div class="user-info-subcontainer">
            <div class="info-block">
              <span>
                <img src="../../../assets/icons/user/id.svg" alt="id-img">
              </span>
              ID: {{ userInfo?.id }}
            </div>
            <div class="info-block">
              <span>
                <img src="../../../assets/icons/user/name.svg" alt="name-img">
              </span>
              Имя пользователя: {{ userInfo?.username }}
            </div>
            <div class="info-block">
              <span>
                <img src="../../../assets/icons/user/money.svg" alt="money-img">
              </span>
              <span>
                Баланс: {{ userInfo?.balance }} ₽
              </span>
              <button
              class="withdraw-button" 
              @click.stop="modalStore.open('top-up-form')"
              >
                Пополнить 
                <img src="../../../assets/icons/user/withdraw-light.svg" alt="withdraw-img">
              </button>
            </div>
          </div>
          <div class="user-info-subcontainer">
            <div class="secondary-container">
              <template v-if="userInfo?.email !== null">
              <div class="info-block">
              <span>
                <img 
                src="../../../assets/icons/user/email.svg" 
                alt="id-img">
              </span>
                Email: {{ userInfo?.email }}
              </div>
              </template>
              <template v-else>
                <div class="info-block">
                  <span>
                    <img 
                    src="../../../assets/icons/user/email.svg" 
                    alt="id-img">
                  </span>
                  Email: Отсутствует
                </div>
              </template>
              <div class="button-block">
                <button @click.stop="modalStore.open('change-email-form')">
                  Изменить email
                </button>
              </div>
            </div>
            <div class="secondary-container">
              <div class="info-block">
              <span>
                <img 
                src="../../../assets/icons/user/passkey.svg" 
                alt="id-img">
              </span>
                Пароль: <input 
                disabled 
                type="password" 
                value="12345678">
              </div>
              <div class="button-block">
                <button @click.stop="modalStore.open('change-password-form')">
                  Изменить пароль
                </button>
              </div>
            </div>
          </div>
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
import keyIcon from "../../../assets/icons/key.svg";
import emailIcon from "../../../assets/icons/mail.svg";
import codeIcon from "../../../assets/icons/lock.svg";
import withdrawIcon from "../../../assets/icons/user/withdraw-dark.svg"
import ModalFormView from "@/components/atom/ModalFormView.vue";
import ChangePasswordFormView from '@/components/atom/ChangePasswordFormView.vue';
import { authorizationService } from '@/services/authorizationService';
import { userService } from '@/services/userService';
import { useModalStore } from '@/stores/useModalStore';
import { UserInfo } from '@/types/userData';
import { computed, ref } from 'vue';

import { AxiosError } from 'axios';
import notification from '@/plugins/notifications';
import { ModalPropsView } from "@/types/component";
import { useRequestHandler } from "@/composable/useRequestHandler";

const props = defineProps<{
  userInfo: UserInfo | null;
}>();

const submitEmailRequest = async(): Promise<void> => {
  if(!newEmail.value || !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(newEmail.value)) {
    console.log('asdasd')
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

const submitTopUpRequest = async(): Promise<void> => {
  if(!amountValue.value){
    notification.error('Введите сумму!');
    return;
  }

  try{
    await userService.requestTopUp(amountValue.value);
    notification.success("Код для подтверждения был отправлен на почту!");
    amountValue.value = null;
    modalStore.close('top-up-form');
  }
  catch(e){
    const axiosError = e as AxiosError;
    console.log(axiosError)
  }
}

const submitEmailCodeConfirm = async(): Promise<void> => {
  useRequestHandler(codeValue).handleConfirm(
    () => userService.confirmUserEmail(codeValue.value),
    "Почта была успешно изменена!",
    'confirm-code-form'
  )
}

const submitTopUpCodeConfirm = async(): Promise<void> => {
  useRequestHandler().handleConfirm(
    () => userService.confirmTopUp(codeValue.value),
    "Баланс был успешно пополнен!",
    'confirm-code-form'
  )
}

const modalStore = useModalStore();
const deviceId = ref();

const newEmail = ref<string>("");
const codeValue = ref<string>("");
const amountValue = ref<number | null>(null);

const modalsConfig: Record<string, ModalPropsView> = {
  'top-up-form': {
    icon: withdrawIcon,
    title: "Введите сумму для пополнения",
    desc: `Введите сумму на которую хотите попольнить ваш аккаунт. 
          После нажатия на кнопку, код будет отправлен на вашу почту.`,
    buttonName: "Подтвердить",
    model: amountValue,
    inputPlaceholder: "5000.00",
    inputType: "text",
    storeKey: "top-up-form",
    submitFunc: submitTopUpRequest
  },
  'change-email-form': {
    icon: emailIcon,
    title: "Введите email",
    desc: `Введите адрес электронной почты, чтобы иметь возможность восстановить доступ к аккаунту. 
    Для подтверждения, вам будет отправлен код на вашу почту`,
    buttonName: "Отправить код",
    model: newEmail,
    inputPlaceholder: "example@example.com",
    inputType: "email",
    storeKey: "change-email-form",
    submitFunc: submitEmailRequest
  },
  'confirm-code-form': {
    icon: codeIcon,
    title: "Введите код подтверждения",
    desc: `Введите код, чтобы привязать новый адрес электронной почты к вашему аккаунту. 
    Для подтверждения, введите этот код снизу. Код был отправлен на указанный вами email`,
    buttonName: "Подтвердить",
    model: codeValue,
    inputPlaceholder: "123456",
    inputType:"text",
    storeKey: "confirm-code-form",
    submitFunc: submitEmailCodeConfirm
  },
  'confirm-withdraw-code-form': {
    icon: keyIcon,
    title: "Введите код подтверждения",
    desc: `Введите код, чтобы подтвердить операцию. 
    Для подтверждения, введите этот код снизу. После нажатия на кнопку, деньги будут на вашем валансе`,
    buttonName: "Подтвердить",
    model: codeValue,
    inputPlaceholder: "123456",
    inputType:"text",
    storeKey: "confirm-withdraw-code-form",
    submitFunc: submitTopUpCodeConfirm
  },
}

const activeModalKey = computed(() => {
  return Object.keys(modalsConfig).find(key => modalStore.isOpen(key));
})

const activeModal = computed<ModalPropsView | null>(() => 
  activeModalKey.value ? modalsConfig[activeModalKey.value] : null
);

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