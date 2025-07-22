package com.codingwitharul.bookmyslot.presentation.ui.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bookmyslot.composeapp.generated.resources.Res
import bookmyslot.composeapp.generated.resources.close
import bookmyslot.composeapp.generated.resources.ic_camera_capture
import bookmyslot.composeapp.generated.resources.ic_camera_rotate
import com.codingwitharul.bookmyslot.common.CameraCallback
import com.codingwitharul.bookmyslot.common.CameraEvent
import com.codingwitharul.bookmyslot.common.CameraView
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import multiplatform.network.cmptoast.showToast
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ImageCaptureView(onImageCaptured: (Path?) -> Unit, onClose: () -> Unit = {}) {

    val scope = rememberCoroutineScope()
    val callback = remember {
        object : CameraCallback() {
            override fun onCaptureImage(image: Path?, error: String?) {
                showToast("Image Captured $image")
                onImageCaptured(image)
            }
        }
    }

    fun takePicture() = scope.launch {
        callback.sendEvent(CameraEvent.CaptureImage)
    }

    fun switchCamera() = scope.launch {
        callback.sendEvent(CameraEvent.SwitchCamera)
    }

    Box(modifier = Modifier.fillMaxSize()) {
//      CameraView from each platform using expect/actual functionality
        CameraView(callback)
//      Custom Capture View Design
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .height(120.dp)
                .background(color = Color.Black.copy(alpha = 0.5f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .size(60.dp),
                onClick = onClose, colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.close),
                    contentDescription = "Settings",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Red
                )
            }
            IconButton(
                onClick = ::takePicture,
                modifier = Modifier
                    .size(80.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_camera_capture),
                    contentDescription = "Take photo",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(
                modifier = Modifier
                    .size(60.dp),
                onClick = ::switchCamera, colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_camera_rotate),
                    contentDescription = "Settings",
                    modifier = Modifier.size(48.dp),
                )
            }
        }
    }
}