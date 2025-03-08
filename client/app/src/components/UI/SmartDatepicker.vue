<script>
import Datepicker from '@vuepic/vue-datepicker';
export default {
  name: "SmartDatepicker",
  components: {
    Datepicker,
  }
}
</script>

<template>
  <Datepicker
      v-model="date"
      placeholder="Когда"
      :enable-time-picker="false"
      :clearable="true"
      auto-apply
      class="custom-datepicker"
  />
  <Datepicker
      v-model="date"
      placeholder="Обратно"
      :enable-time-picker="false"
      :clearable="true"
      auto-apply
      class="custom-datepicker"
  />
</template>

<style scoped lang="sсss">
@import "@/assets/styles/objects/smart-datepicker.sсss"
</style>