import { Route } from "./route";

export interface BookingResponse {
    id: number;
    userId: number;
    route: Route;
    payments: [];
    status: string;
    succesfullPayment: string | null;
}