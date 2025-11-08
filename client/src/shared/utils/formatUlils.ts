
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

    const formatBookingStatus = (status: string) => {
        switch(status) {
            case 'PAID': return 'Оплачен'
            case 'PENDING': return 'В обработке'
            case 'CANCELED': return 'Отменен'
            default: return 'ОШИБКА'
        }
    }

    return {
        formatTransportStringToEmoji,
        formatISOString,
        formatBookingStatus
    }
}