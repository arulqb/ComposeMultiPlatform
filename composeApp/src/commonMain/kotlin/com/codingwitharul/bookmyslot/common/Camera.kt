package com.codingwitharul.bookmyslot.common

import androidx.compose.runtime.Composable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.io.files.Path

sealed class CameraEvent {
    object CaptureImage : CameraEvent()
    object SwitchCamera : CameraEvent()
}

abstract class CameraCallback {
    private val _event = Channel<CameraEvent>()
    val eventFlow: Flow<CameraEvent> get() = _event.receiveAsFlow()
    suspend fun sendEvent(event: CameraEvent) {
        this._event.send(event)
    }
    abstract fun onCaptureImage(image: Path?, error: String? = null)
}

@Composable
expect fun CameraView(callback: CameraCallback)