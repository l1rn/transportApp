interface ImportMetaEnv{
    readonly VITE_BACKEND_URL: string;
    readonly VITE_GATEWAY_URL: string;
    readonly VITE_FRONTEND_PORT: string;
    readonly VITE_HMAC_KEY: string;
    readonly VITE_JWT_SIGNING_KEY: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}