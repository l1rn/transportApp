
export interface OrderInfoResponse {
    orderFullName: string;
    price: number;
    paymentMethods: Array<string>;
    hasEmail: boolean;
    inProgress: boolean;
    paid: boolean;
}

export interface PaymentRouteData { 
    id: number;
    name: string;
    description: string;
}

export interface PaymentData {
    id: number;
    amount: number;
    method: string;
    description: string;
    createdAt: string;
    username: string;
    status: string;
    route: Array<PaymentRouteData>;
}

export interface PaymentHistoryResponse {
    id: number;
    amount: number;
    method: string;
    description: string;
    createdAt: string;
    username: string;
    routeId: number;
}