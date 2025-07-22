package com.codingwitharul.bookmyslot.presentation.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bookmyslot.composeapp.generated.resources.Res
import bookmyslot.composeapp.generated.resources.app_name
import bookmyslot.composeapp.generated.resources.logo_bml
import bookmyslot.composeapp.generated.resources.stallion_beatsides_regular
import com.codingwitharul.bookmyslot.common.HardwareInfo
import com.codingwitharul.bookmyslot.db.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import multiplatform.network.cmptoast.showToast
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview
@Composable
internal fun SplashScreen(onNavigate: (UserInfo?) -> Unit = {}) {

    val viewModel: SplashScreenViewModel = koinInject()
    val state by viewModel.uiState.collectAsState()
    var splashAnime by remember { mutableStateOf(true) }

    val hardInfor: HardwareInfo = koinInject()
    showToast("Screen size ${hardInfor.getHardwareInfo().height}")
    LaunchedEffect(viewModel.uiState) {
        delay(600)
        splashAnime = false
        delay(700)
        viewModel.uiState.filter { !it.loading }.collect {
            onNavigate(state.userInfo)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Circle(modifier = Modifier.align(Alignment.TopCenter))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                splashAnime,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Text(
                    stringResource(Res.string.app_name), fontFamily = FontFamily(
                        Font(
                            resource = Res.font.stallion_beatsides_regular,
                            weight = FontWeight.Normal,
                            style = FontStyle.Normal
                        )
                    ),
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 58.sp
                )
            }

            AnimatedVisibility(
                splashAnime,
                enter = fadeIn(animationSpec = tween(durationMillis = 600)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1500))
            ) {
                val widthAnim =
                    animateDpAsState(targetValue = 230.dp)
                val heightAnim =
                    animateDpAsState(targetValue = 200.dp)

                val infiniteTransition = rememberInfiniteTransition(label = "floating")

                val offsetY by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 20f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = "offsetY"
                )

                Image(
                    painter = painterResource(Res.drawable.logo_bml),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.width(widthAnim.value).height(heightAnim.value)
                        .offset(y = offsetY.dp)
                )
            }
        }
        CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))

    }

}

@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    radiusEnd: Float = 1800f
) {

    val targetValue = produceState(initialValue = 0f) {
        delay(500)
        value = radiusEnd
    }

    val floatAnim = animateFloatAsState(
        animationSpec = keyframes {
            durationMillis = 200
        },
        targetValue = targetValue.value
    )

    Canvas(modifier = modifier) {
        val centerOffset = Offset(size.width / 2, size.height / 2)

        drawCircle(
            color = color,
            radius = floatAnim.value,
            center = centerOffset
        )
    }
}


