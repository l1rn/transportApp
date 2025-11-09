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
        
        const handleEscClick = (event: KeyboardEvent) => {
            if(!isActive()) return;

            if(event.key === 'Escape' || event.key === 'Esc'){
                callback();
            }
        }

        onMounted(() => {
            document.addEventListener('click', handleClickOutside);
            document.addEventListener('keydown', handleEscClick);
        })
        onBeforeUnmount(() => {
            document.removeEventListener('click', handleClickOutside);
            document.removeEventListener('keydown', handleEscClick);
        })
    }