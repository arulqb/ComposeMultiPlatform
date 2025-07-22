package com.codingwitharul.bookmyslot.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureStillImageOutput
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVVideoCodecKey
import platform.AVFoundation.position
import platform.CoreGraphics.CGRect
import platform.Foundation.writeToFile
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIDevice
import platform.UIKit.UIView


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView(callback: CameraCallback) {
    val session = AVCaptureSession()
    session.sessionPreset = AVCaptureSessionPresetPhoto
    val output = AVCaptureStillImageOutput().apply {
        outputSettings = mapOf(AVVideoCodecKey to platform.AVFoundation.AVVideoCodecJPEG)
    }
    session.addOutput(output)
    val cameraPreviewLayer = AVCaptureVideoPreviewLayer(session = session)
    LaunchedEffect(Unit) {
        val backCamera =
            AVCaptureDevice.devicesWithMediaType(AVMediaTypeVideo).firstOrNull { device ->
                (device as AVCaptureDevice).position == AVCaptureDevicePositionBack
            } as? AVCaptureDevice ?: return@LaunchedEffect

        val frontCamera =
            AVCaptureDevice.devicesWithMediaType(AVMediaTypeVideo).firstOrNull { device ->
                (device as AVCaptureDevice).position == AVCaptureDevicePositionFront
            } as? AVCaptureDevice ?: return@LaunchedEffect

        var currentCamera = backCamera
        var currentInput =
            AVCaptureDeviceInput.deviceInputWithDevice(currentCamera, null) as AVCaptureDeviceInput
        session.addInput(currentInput)
        session.addOutput(output)
        session.startRunning()

        callback.eventFlow.collect {
            when (it) {
                CameraEvent.CaptureImage -> {
                    val connection = output.connectionWithMediaType(AVMediaTypeVideo)
                    if (connection != null) {
                        output.captureStillImageAsynchronouslyFromConnection(connection) { sampleBuffer, error ->
                            if (sampleBuffer != null && error == null) {
                                val imageData =
                                    AVCaptureStillImageOutput.jpegStillImageNSDataRepresentation(
                                        sampleBuffer
                                    )
                                if (imageData != null) {
                                    val filePath =
                                        platform.Foundation.NSTemporaryDirectory() + UIDevice.currentDevice.identifierForVendor?.UUIDString + ".jpg"
                                    imageData.writeToFile(filePath, true)
                                    callback.onCaptureImage(Path(filePath))
                                }
                            }
                        }
                    }
                }

                CameraEvent.SwitchCamera -> {
                    session.stopRunning()
                    session.removeInput(currentInput)
                    currentCamera = if (currentCamera == backCamera) frontCamera else backCamera
                    currentInput = AVCaptureDeviceInput.deviceInputWithDevice(
                        currentCamera,
                        null
                    ) as AVCaptureDeviceInput
                    session.addInput(currentInput)
                    session.startRunning()
                }
            }
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        background = Color.Black,
        factory = {
            val container = UIView()
            container.layer.addSublayer(cameraPreviewLayer)
            cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            container
        },
        onResize = { container: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            container.layer.setFrame(rect)
            cameraPreviewLayer.setFrame(rect)
            CATransaction.commit()
        })
}