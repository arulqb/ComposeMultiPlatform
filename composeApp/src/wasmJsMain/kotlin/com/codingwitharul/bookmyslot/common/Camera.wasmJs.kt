package com.codingwitharul.bookmyslot.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.mediacapture.MediaStreamConstraints

@Composable
actual fun CameraView(callback: CameraCallback) {
    val video = document.createElement("video") as HTMLVideoElement
    video.setAttribute("playsinline", "") // Required to work on iOS
    video.setAttribute("autoplay", "") // Required to work on iOS
    LaunchedEffect(Unit) {
        callback.eventFlow.collect {

        }
    }
    window.navigator.mediaDevices.getUserMedia(
        constraints = MediaStreamConstraints(
            video = true.toJsBoolean(),
            audio = true.toJsBoolean()
        )
    ).then(onFulfilled = { stream ->
        video.srcObject = stream
        video.addEventListener("loadedmetadata") { event ->

        }
        video
    }, onRejected = {
        video
    })
}