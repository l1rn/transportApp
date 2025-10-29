import { AxiosResponse } from "axios";
import { api } from "./api";
import { PaymentMethod } from "@/types/payment";

class PaymentService{
    public async getOrderInfo(bookingId: number): Promise<AxiosResponse> {
        return await api.get(`/payments/get-info?bookingId=${bookingId}`);
    }

    public async createPayment(bookingId: number, paymentMethod: PaymentMethod)    
        : Promise<AxiosResponse> {
            return await api.post(`/payments/create?bookingId=${bookingId}&paymentMethod=${paymentMethod}`)
        }

    public async confirmPayment(externalId: string, code: string)
        :Promise<AxiosResponse> {
            return await api.post(`/payment/confirm`, {
                externalId: externalId,
                code: code
            })
        }
}