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
              <div class="info-block password-block">
                <span>
                  <img 
                  src="../../../assets/icons/user/passkey.svg" 
                  alt="id-img">
                </span>
                Пароль: 
                <input 
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
              class="useragent-container"
            >
              {{ props.userInfo?.currentDevice.userAgent }}
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
import { authorizationService } from '@/shared/services/authorizationService';
import { userService } from '@/shared/services/userService';
import { useModalStore } from '@/shared/stores/useModalStore';
import { UserInfo } from '@/shared/types/userData';
import { computed, ref } from 'vue';

import { ModalPropsView } from "@/shared/types/component";
import { useRequestHandler } from "@/composable/useRequestHandler";
import notification from "@/shared/plugins/notifications";

const props = defineProps<{
  userInfo: UserInfo | null;
}>();

const submitEmailRequest = async(): Promise<void> => {
  useRequestHandler().handleRequest(
    () => userService.requestUserEmail(newEmail.value),
    "Код с подтверждением отправлен вам на почту!",
    "Введите email!",
    'confirm-code-form',
    'change-email-form',
    newEmail,
    !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(newEmail.value),
  )
}

const submitTopUpRequest = async(): Promise<void> => {
  useRequestHandler().handleRequest(
    () => userService.requestTopUp(amountValue.value),
    "Код с подтверждением отправлен вам на почту!",
    "Введите сумму!",
    'confirm-withdraw-code-form',
    'top-up-form',
    amountValue,
  )
}

const submitEmailCodeConfirm = async(): Promise<void> => {
  useRequestHandler().handleConfirm(
    () => userService.confirmUserEmail(codeValue.value),
    "Почта была успешно изменена!",
    'confirm-code-form',
    codeValue
  )
  window.location.reload();
}

const submitTopUpCodeConfirm = async(): Promise<void> => {
  useRequestHandler().handleConfirm(
    () => userService.confirmTopUp(withdrawCodeValue.value),
    "Баланс был успешно пополнен!",
    'confirm-withdraw-code-form',
    withdrawCodeValue
  )
}

const modalStore = useModalStore();

const newEmail = ref<string>("");
const codeValue = ref<string>("");
const withdrawCodeValue = ref<string>("");
const amountValue = ref<number | null>(null);

const modalsConfig: Record<string, ModalPropsView> = {
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
  'top-up-form': {
    icon: withdrawIcon,
    title: "Введите сумму для пополнения",
    desc: `Введите сумму на которую хотите попольнить ваш аккаунт. 
          После нажатия на кнопку, код будет отправлен на вашу почту.`,
    buttonName: "Отправить код",
    model: amountValue,
    inputPlaceholder: "5000.00",
    inputType: "text",
    storeKey: "top-up-form",
    submitFunc: submitTopUpRequest
  },
  'confirm-withdraw-code-form': {
    icon: keyIcon,
    title: "Введите код подтверждения",
    desc: `Введите код, чтобы подтвердить операцию. 
    Для подтверждения, введите этот код снизу. После нажатия на кнопку, деньги будут на вашем валансе`,
    buttonName: "Подтвердить",
    model: withdrawCodeValue,
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
    if(props.userInfo?.currentDevice.id === id) {
      await userService.deleteSession(id);
      await authorizationService.logoutUser();
    }
  }
  catch(e){
    console.error(e);
    notification.error("Не удалось удалить сессию!");
  }
}
</script>
<style scoped lang="sass">
@use '../../../assets/styles/molecule/profile/user-setting.sass'
</style>