<script setup>
import { ref, reactive, defineEmits } from "vue";
import Datepicker from "@vuepic/vue-datepicker";
const emit = defineEmits(['select'])

const isOpen = ref(false)
const selectedTransport = ref(null)
const transports = reactive([
  { value: 'train', label: 'Поезд', emoji: '🚂' },
  { value: 'bus', label: 'Автобус', emoji: '🚌' },
  { value: 'air', label: 'Авиа', emoji: '✈️' }
])

const toggleMenu = () => {
  isOpen.value = !isOpen.value
}

const selectTransport = (value) => {
  const transport = transports.find(t => t.value === value)
  selectedTransport.value = transport?.label || null
  isOpen.value = false
  emit('select', value)
}
</script>
<template>
  <div class="main-container">
    <input class="b-form-input" placeholder="Откуда" />
    <input class="b-form-input ms-1" placeholder="Куда" />
    <Datepicker
        v-model="date"
        placeholder="Когда"
        :format="'dd-MM-yyyy'"
        :dark="false"
        :enable-time-picker="false" />
    <Datepicker class=""
                v-model="arrivalDate"
                placeholder="Обратно"
                :format="'dd-MM-yyyy'"
                :enable-time-picker="false" />

    <div class="transport-wrapper">
      <div
          class="transport-toggle"
          :class="{ 'active': isOpen }"
          @click="toggleMenu"
      >
      <span class="current-transport">
        {{ selectedTransport || 'Транспорт' }}
      </span>
        <span class="arrow">▼</span>
      </div>

      <transition name="slide-fade">
        <div v-if="isOpen" class="transport-menu">
          <div
              v-for="transport in transports"
              :key="transport.value"
              class="transport-item"
              @click="selectTransport(transport.value)"
          >
            <span class="emoji">{{ transport.emoji }}</span>
            {{ transport.label }}
          </div>
        </div>
      </transition>
    </div>
    <button class="search-button-custom btn"
            :class="{'opacity-50': loading}"
            :disabled="loading">

      <span v-if="!loading">Поиск</span>
      <span v-else>⌛</span>
      <span class="search-icon">🔍</span>
    </button>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/objects/smart-input"
</style>