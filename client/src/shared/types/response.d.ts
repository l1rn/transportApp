export interface PaginatedResponse {
    content: Array<T>;
    pageSize: number;
    totalElements: number;
    totalPages: number;
}