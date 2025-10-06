<script setup>
import InputView from "../usercomponents/InputView.vue";

</script>
<template>
  <div class="main-search-container">
    <div class="search-wrapper">
      <div class="input-wrapper">
        <input-view />
        <input-view />
      </div>

      <div class="input-wrapper">
        <input-view />
        <input-view />
      </div>
      <div class="search-wrapper">
        <button>Search</button>
        <button>X</button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="sass">
@import "@/assets/styles/objects/smart-input"
</style>