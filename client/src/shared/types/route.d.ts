
export interface Route { 
    id?: number;
    routeFrom: string;
    routeTo: string;
    transport: string;
    destinationTime: string;
    arrivalTime: string;
    availableSeats: number | null;
    price: number | null;
} 

export interface RouteFilter {
  routeFrom?: string;
  routeTo?: string;
  date?: string;
  transport?: string;
}

export interface PaginatedRoute {
    totalPages: number;
    contentPage: number;
    content: Array<Route>;
    totalElements: number;
}