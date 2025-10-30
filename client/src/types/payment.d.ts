
export interface OrderInfoResponse {
    orderFullName: string;
    price: number;
    paymentMethods: Array<string>;
    hasEmail: boolean;
}