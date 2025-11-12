import { watchEffect } from "vue";

export const useCheckOverflow = () => {
  const watchUnderModal = (isActive: boolean) => {
    watchEffect(() => {
      if (isActive) {
        document.body.style.overflowY = "hidden";
        document.documentElement.style.overflowY = "hidden";
      } else {
        document.body.style.overflowY = "auto";
        document.documentElement.style.overflowY = "auto";
      }
    });
  };
  return { watchUnderModal }
};
