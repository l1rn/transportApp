import { ref } from "vue";

export function useTypeDetection(delay: number = 350) {
    const isTyping = ref(false);
    const isWaiting = ref(false);

    let typingTimer: number | null = null;

    const onTypeStart = () => {
        isTyping.value = true;
        isWaiting.value = true;

        if(typingTimer){
            clearTimeout(typingTimer);
        }
    };

    const onTypeEnd = (callback: () => void) => {
        typingTimer = setTimeout(() => {
            isTyping.value = false;
            isWaiting.value = false;
            callback();
        }, delay);
    };

    const cancelTypeDetection = () => {
        if(typingTimer){
            clearTimeout(typingTimer);
            isTyping.value = false;
            isWaiting.value = false;
        }
    }

    return {
        isTyping,
        isWaiting,
        onTypeStart,
        onTypeEnd,
        cancelTypeDetection
    };
}