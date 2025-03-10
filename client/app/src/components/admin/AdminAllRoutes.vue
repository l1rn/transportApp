<template>
    <div>
        <div>
            <AddRoute></AddRoute>
        </div>
        <div>
            <DeleteRoute></DeleteRoute>
        </div>
        <div>
            <PutRoute></PutRoute>
        </div>
    </div>
</template>
<script setup>
import AddRoute from './AddRoute.vue';
import DeleteRoute from './DeleteRoute.vue';
import PutRoute from './PutRoute.vue';

</script>
<script>
export default {
    name: "AdminAllRoutes"
}
</script>
<style lang="">
    
</style>