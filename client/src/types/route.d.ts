
export interface Route { 
    id?: number;
    routeFrom: string;
    routeTo: string;
    date: string;
    transport: string;
    time: string;
    arrivalTime: string;
    availableSeats: number;
    price: number;
}

export interface PaginatedRoute {
    totalPages: number;
    contentPage: number;
    content: Array<Route>;
    totalElements: number;
}