import { AxiosResponse } from "axios";
import { api } from "./api";
import { OrderInfoResponse, PaymentData, PaymentHistoryResponse } from "../types/payment";
import { PaginatedResponse } from "../types/response";

class PaymentService{
    public async getMyPayments(): Promise<AxiosResponse<PaginatedResponse<PaymentData>>>{
        return await api.get(`/payments/get-my?page=0&size=5`);
    }

    public async getOrderInfo(bookingId: number)
        : Promise<AxiosResponse<OrderInfoResponse>> {
        return await api.get(`/payments/get-info?bookingId=${bookingId}`);
    }
    
    public async getExternalId(bookingId: number): Promise<AxiosResponse<string | null>>{
        return await api.get(`/payments/get-id?bookingId=${bookingId}`);
    }

    public async createPayment(bookingId: number, paymentMethod: string)    
        : Promise<AxiosResponse> {
            return await api.post(`/payments/create?bookingId=${bookingId}&paymentMethod=${paymentMethod}`);
        }

    public async resendConfirmationCode(paymentId: string): Promise<AxiosResponse>{
        return await api.post(`/payments/resend-code?paymentId=${paymentId}`);
    }

    public async confirmPayment(externalId: string | null, code: string)
        :Promise<AxiosResponse> {
            return await api.post(`/payments/confirm`, {
                externalId: externalId,
                code: code
            })
        }
    
    public async cancelPayment(externalId: string | null): Promise<AxiosResponse> {
        return await api.post(`/payments/cancel?externalId=${externalId}`);
    }

    public async getPaymentHistoryByBookingId(
        bookingId: number, 
        page: number = 0, 
        size: number = 10
    ): Promise<AxiosResponse<PaginatedResponse<PaymentHistoryResponse>>> {
        return await api.get(`/payments/history?bookingId=${bookingId}&page=${page}&size=${size}`);
    } 
}

export const paymentService = new PaymentService();