<script setup>
import { reactive, ref } from 'vue';
  const counter = reactive({
    name: "Jogn",
    count:0
  })
  const message = ref('Hello 1232!')
</script>

<template>
  <h1>{{ message.split('').reverse().join('') }}</h1>
  <p>Count is: {{ counter.count }}</p>
  <p>Name ++ {{counter.name}}</p>

  <button @click="name.count++">+name</button>
  <button @click="counter.count++">increase</button>

</template>