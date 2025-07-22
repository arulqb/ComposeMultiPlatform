package com.codingwitharul.bookmyslot.common

data class HardwareInfoData(val width: Int, val height: Int)

expect class HardwareInfo {
    fun getHardwareInfo(): HardwareInfoData
}