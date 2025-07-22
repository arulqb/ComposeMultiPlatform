//package com.codingwitharul.bookmyslot.presentation.ui.dragdrop
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.draganddrop.dragAndDropSource
//import androidx.compose.foundation.gestures.draggable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draganddrop.DragAndDropTransferData
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.drawText
//import androidx.compose.ui.text.rememberTextMeasurer
//import androidx.compose.ui.unit.dp
//
//@ExperimentalComposeUiApi
//@Composable
//fun DragSource() {
//    val exportedText = "Hello, drag and drop!"
//
//    val textMeasurer = rememberTextMeasurer()
//
//    Box(Modifier
//        .dragAndDropSource(
//            drawDragDecoration = {
//                drawText(
//                    textMeasurer,
//                    AnnotatedString(exportedText),
//                )
//            },
//            transferData = { offset ->
//                DragAndDropTransferData(
//                    items = {
//
//                    }
//                )
//
//            },
//        ).draggable(state = ) {
//            detectDragGestures(
//                onDragStart = { offset ->
//                    startTransfer(
//                        // Defines transferable data and supported transfer actions.
//                        // When an action is concluded, prints the result into
//                        // system output with onTransferCompleted().
//                        DragAndDropTransferData(
//                            transferable = DragAndDropTransferable(
//                                StringSelection(exportedText)
//                            ),
//
//                            // List of actions supported by this drag source. A type of action
//                            // is passed to the drop target together with data.
//                            // The target can use this to reject an inappropriate drop operation
//                            // or to interpret user expectations.
//                            supportedActions = listOf(
//                                DragAndDropTransferAction.Copy,
//                                DragAndDropTransferAction.Move,
//                                DragAndDropTransferAction.Link,
//                            ),
//                            dragDecorationOffset = offset,
//                            onTransferCompleted = { action ->
//                                println("Action at the source: $action")
//                            }
//                        )
//                    )
//                },
//                onDrag = { _, _ -> },
//            )
//        }
//        .size(200.dp)
//        .background(Color.LightGray)
//    ) {
//        Text("Drag Me", Modifier.align(Alignment.Center))
//    }
//}