export interface PaginatedResponse<T> {
    content: Array<T>;
    pageSize: number;
    totalElements: number;
    totalPages: number;
}