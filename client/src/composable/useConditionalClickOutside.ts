import { inject, onBeforeUnmount, onMounted, Ref } from "vue";

export const useConditionalClickOutside = 
    (
        elementRef: Ref<HTMLElement | null>,
        isActive: () => boolean,
        callback: () => void
    ) => {
        const handleClickOutside = (event: MouseEvent | KeyboardEvent) => {
            if(!isActive()) return;
            
            if(event instanceof KeyboardEvent){
                if(event.key === 'Escape' || event.key === 'Esc'){
                    callback();
                    return;
                }
            }

            if(event instanceof MouseEvent){
                const target = event.target as HTMLElement;
                if(elementRef.value && !elementRef.value.contains(target)){
                    callback();
                }
            }
        }

        onMounted(() => {
            document.addEventListener('click', handleClickOutside);
        })
        onBeforeUnmount(() => {
            document.removeEventListener('click', handleClickOutside);
        })
    }