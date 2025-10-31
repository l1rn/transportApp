import { AxiosResponse } from "axios";
import { api } from "./api";

class PaymentService{
    public async getOrderInfo(bookingId: number): Promise<AxiosResponse> {
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