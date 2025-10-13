
export interface Device {
    id: number;
    deviceFingerPrint: string;
    userAgent: string;
}

export interface UserData {
    username: string;
    password: string;
}

export interface UserInfo {
    id: number;
    username: string;
    role: string;
    devices: Array<Device>;
}