import { Route } from "./route";

export interface AdminGetAllBookings {
    id: number;
    routeId: number;
    routeFrom: string;
    routeTo: string;
    destinationTime: string;
    arrivalTime: string;
    transport: string;
    username: string;
    status: string;
    price: number;
}

export interface BookingResponse {
    id: number;
    userId: number;
    route: Route;
    payments: [];
    status: string;
    succesfullPayment: string | null;
}