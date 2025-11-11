import { Route, RouteFilter } from "../types/route";
import pendingIcon from "../../assets/icons/payment/statuses/pending.svg";
import succeededIcon from "../../assets/icons/payment/statuses/succeeded.svg";
import failedIcon from "../../assets/icons/payment/statuses/failed.svg";
import cancelledIcon from "../../assets/icons/payment/statuses/cancelled.svg";

export const useFormatUtils = () => {
    const formatTransportStringToEmoji = (transport: string) => {
        switch(transport) {
            case 'Поезд': return '🚂'
            case 'Авиа': return '✈️'
            case 'Автобус': return '🚌'
            default: return ''
        }
    }

    const formatISOString = (isoString: string): string => {
        const date = new Date(isoString);
        return date.toLocaleDateString([], {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        });
    }
    const removeEmojiFromRouteData = (transport: string) => {
        const removeEmoji = (s?: string) => s?.replace(/^[^\p{L}\p{N}]+/u, '').trim();
        return transport === "Любой" ? "" : removeEmoji(transport)
    }

    const removeEmojiForTransport = (f: RouteFilter | Route): RouteFilter => {
        const removeEmoji = (s?: string) => s?.replace(/^[^\p{L}\p{N}]+/u, '').trim();

        return {
            ...f,
            transport: removeEmoji(f?.transport) === "Любой" ? "" : removeEmoji(f?.transport)
        }
    }

    const formatBookingStatus = (status: string) => {
        switch(status) {
            case 'PAID': return 'Оплачен'
            case 'PENDING': return 'В обработке'
            case 'CANCELED': return 'Отменен'
            default: return 'ОШИБКА'
        }
    }
    
    const getStatusIconFromBookingStatus = (status: string): string => {
        switch(status) {
            case 'PAID': return succeededIcon;
            case 'PENDING': return pendingIcon;
            case 'CANCELLED': return cancelledIcon;
            default: return pendingIcon
        }
    }

    return {
        removeEmojiForTransport,
        removeEmojiFromRouteData,
        formatTransportStringToEmoji,
        formatISOString,
        formatBookingStatus,
        getStatusIconFromBookingStatus
    }
}