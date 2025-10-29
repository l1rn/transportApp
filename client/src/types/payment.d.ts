
export enum PaymentMethod {
    CARD = "CARD",
    BANK_TRANSFER = "BANK_TRANSFER",
    ELECTRONIC = "ELECTRONIC",
    SIMULATION = "SIMULATION"
}

export interface OrderInfoResponse {
    orderFullName: string;
    price: number;
    paymentMethods: Array<PaymentMethod>;
    hasEmail: boolean;
}