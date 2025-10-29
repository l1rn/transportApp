
export enum PaymentMethod {
    CARD,
    BANK_TRANSFER,
    ELECTRONIC,
    SIMULATION
}

export interface OrderInfoResponse {
    orderFullName: string;
    price: number;
    paymentMethods: Array<PaymentMethod>;
    hasEmail: boolean;
}