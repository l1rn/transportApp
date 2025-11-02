import { AxiosResponse } from "axios";
import { api } from "./api";
import { OrderInfoResponse, PaginatedPayment } from "../types/payment";

class PaymentService{
    public async getMyPayments(): Promise<AxiosResponse<PaginatedPayment>>{
        return await api.get(`/payments/get-my`);
    }

    public async getOrderInfo(bookingId: number): Promise<AxiosResponse<OrderInfoResponse>> {
        return await api.get(`/payments/get-info?bookingId=${bookingId}`);
    }

    public async createPayment(bookingId: number, paymentMethod: string)    
        : Promise<AxiosResponse> {
            return await api.post(`/payments/create?bookingId=${bookingId}&paymentMethod=${paymentMethod}`)
        }

    public async confirmPayment(externalId: string | undefined, code: string)
        :Promise<AxiosResponse> {
            return await api.post(`/payments/confirm`, {
                externalId: externalId,
                code: code
            })
        }
}

export const paymentService = new PaymentService();