
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
    email: string | null;
    role: string;
    balance: number;
    currentDevice: Device;
    devices: Array<Device>;
}

export interface ChangePasswordRequest {
    oldPassword: string;
    newPassword: string;
}