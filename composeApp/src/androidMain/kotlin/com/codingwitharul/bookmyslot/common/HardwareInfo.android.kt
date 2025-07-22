package com.codingwitharul.bookmyslot.common

import android.content.Context

actual class HardwareInfo(val context: Context) {
    actual fun getHardwareInfo(): HardwareInfoData {
        return HardwareInfoData(
            width = context.resources.displayMetrics.widthPixels,
            height = context.resources.displayMetrics.heightPixels,
        )
    }
}