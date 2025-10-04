import { onBeforeUnmount, onMounted, Ref } from "vue";

export const useConditionalClickOutside = 
    (
        elementRef: Ref<HTMLElement | null>,
        isActive: () => boolean,
        callback: () => void
    ) => {
        const handleClickOutside = (event: MouseEvent) => {
            if(!isActive()) return;

            const target = event.target as HTMLElement;
            if(elementRef.value && !elementRef.value.contains(target)){
                callback();
            }
        }

        onMounted(() => {
            document.addEventListener('click', handleClickOutside);
        })
        onBeforeUnmount(() => {
            document.removeEventListener('click', handleClickOutside);
        })
    }