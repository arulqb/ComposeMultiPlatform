package com.codingwitharul.bookmyslot.common

actual class HardwareInfo {
    actual fun getHardwareInfo(): HardwareInfoData {
        return HardwareInfoData(12, 12)
    }
}