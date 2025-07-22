//package com.codingwitharul.bookmyslot.presentation.ui.dragdrop
//
//import android.content.ClipData
//import android.content.ClipDescription
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.draganddrop.dragAndDropSource
//import androidx.compose.foundation.draganddrop.dragAndDropTarget
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draganddrop.DragAndDropEvent
//import androidx.compose.ui.draganddrop.DragAndDropTarget
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.BlendMode.Companion.Screen
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.ClipEntry
//import androidx.compose.ui.platform.ClipMetadata
//import androidx.compose.ui.platform.toClipData
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.drawText
//import androidx.compose.ui.text.rememberTextMeasurer
//import androidx.compose.ui.unit.dp
//import bookmyslot.composeapp.generated.resources.Res
//import bookmyslot.composeapp.generated.resources.ic_drag_drop
//import bookmyslot.composeapp.generated.resources.img_headphone_splash
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import org.jetbrains.compose.resources.painterResource
//
//@Composable
//fun DragAndDropScreen() {
//    val exportedText = "Hello, drag and drop!"
//
//    val textMeasurer = rememberTextMeasurer()
//    var showTargetBorder by remember { mutableStateOf(false) }
//    var targetText by remember { mutableStateOf("Drop Here") }
//    val coroutineScope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Draggable Image
//        Image(
//            painter = painterResource( Res.drawable.ic_drag_drop), // Replace with your image resource
//            contentDescription = "Draggable Image",
//            modifier = Modifier
//                .size(100.dp)
//                .dragAndDropSource { t ->
//
//                    detectTapGestures(
//                        onLongPress = {
//                            startTransfer(
//                                _root_ide_package_.androidx.compose.ui.draganddrop.DragAndDropTransferData(
//                                    clipData = ClipData(
//                                        ClipDescription(
//                                            "Image",
//                                            arrayOf("image/jpeg")
//                                        ), // Provide appropriate mime type
//                                        ClipData.Item("Image URI or data") // Provide image URI or data
//                                    ),
//                                    flags = android.view.View.DRAG_FLAG_GLOBAL,
//                                    localState = "Dragging image" // Optional local state
//                                )
//                            )
//                        }
//                    )
//                }
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Drop Target
//        Box(
//            modifier = Modifier
//                .size(200.dp)
//                .background(if (showTargetBorder) Color.Green.copy(alpha = 0.5f) else Color.LightGray)
//                .dragAndDropTarget(
//                    shouldStartDragAndDrop = { event ->
//                        // Only accept image drops
//                        event
//                            .mimeTypes()
//                            .any { it.startsWith("image/") }
//                    },
//                    target = object : DragAndDropTarget {
//                        override fun onDrop(event: DragAndDropEvent): Boolean {
//                            val clipData = event.toClipData()
//                            if (clipData.itemCount > 0) {
//                                val item = clipData.getItemAt(0)
//                                // Handle the dropped image data (e.g., display it, save URI)
//                                // For simplicity, we just change the text
//                                targetText = "Image Dropped!"
//                                // You might want to access item.uri or item.text depending on how you set it up
//                            }
//                            showTargetBorder = false
//                            return true // Return true if the drop was accepted
//                        }
//
//                        override fun onEntered(event: DragAndDropEvent) {
//                            showTargetBorder = true
//                            targetText = "Release to drop"
//                        }
//
//                        override fun onExited(event: DragAndDropEvent) {
//                            showTargetBorder = false
//                            targetText = "Drop Here"
//                        }
//
//                        override fun onEnded(event: DragAndDropEvent) {
//                            showTargetBorder = false
//                            // Optionally reset targetText if the drop didn't happen on this target
//                            // if (!event.isConsumed) targetText = "Drop Here"
//                        }
//                    }
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = targetText)
//        }
//    }
//}