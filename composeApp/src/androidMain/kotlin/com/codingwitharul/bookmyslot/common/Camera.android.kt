package com.codingwitharul.bookmyslot.common

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions.Builder
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import java.io.File
import java.util.concurrent.Executor

@Composable
actual fun CameraView(callback: CameraCallback) {

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    var cameraLens by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    fun takePicture() {
        val photoFile = File(
            getOutputDirectory(context),
            "${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = Builder(photoFile).build()
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        imageCapture.takePicture(
            outputOptions,
            mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("TAG", "Photo capture failed: ${exc.message}", exc)
                    scope.launch {
                        callback.onCaptureImage(error = exc.message, image = null)
                    }
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    Log.d("TAG", "Photo capture succeeded: $savedUri")
                    scope.launch {
                        callback.onCaptureImage(savedUri.path?.let { path -> Path(path) })
                    }
                }
            }
        )
    }

    fun switchLens() {
        cameraLens = if (cameraLens == CameraSelector.LENS_FACING_BACK)
            CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
    }

    LaunchedEffect(Unit) {
        callback.eventFlow.collect {
            when (it) {
                CameraEvent.CaptureImage -> takePicture()
                CameraEvent.SwitchCamera -> switchLens()
            }
        }
    }

    LaunchedEffect(lifeCycleOwner, context, cameraLens) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraLens)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifeCycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                Log.d("CameraView", "Camera use cases bound successfully.")

            } catch (exc: Exception) {
                Log.e("CameraView", "Use case binding failed", exc)
                scope.launch {
                    callback.onCaptureImage(error = exc.message, image = null)
                }
            }
        }, ContextCompat.getMainExecutor(context))
    }
    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize(),
    )

    DisposableEffect(Unit) {
        onDispose {
            Log.d("CameraView", "Disposing CameraView, unbinding camera use cases.")
            try {
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
                // previewView.surfaceProvider = null
            } catch (e: Exception) {
                Log.e("CameraView", "Error during camera unbinding/cleanup", e)
            }
        }
    }
}

/**
 *
 * Store photo in private folder.
 *
 * App's internal files directory. No special permissions needed.
 */
private fun getOutputDirectory(context: Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, "bms").apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) {
        mediaDir
    } else {
        context.filesDir
    }
}