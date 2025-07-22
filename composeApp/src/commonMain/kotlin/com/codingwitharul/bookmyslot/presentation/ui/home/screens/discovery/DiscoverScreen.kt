package com.codingwitharul.bookmyslot.presentation.ui.home.screens.discovery

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import bookmyslot.composeapp.generated.resources.Res
import bookmyslot.composeapp.generated.resources.logo_bml
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.codingwitharul.bookmyslot.domain.model.OfferModel
import com.codingwitharul.bookmyslot.domain.model.ProductModel
import com.codingwitharul.bookmyslot.domain.model.ServiceModel
import com.codingwitharul.bookmyslot.presentation.components.AppTopAppBar
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview
@Composable
fun DiscoverScreen(windowWidthSizeClass: WindowWidthSizeClass) {
    val viewModel: DiscoverViewModel = koinInject()
    val state by viewModel.uiState.collectAsState()

    val isTab = windowWidthSizeClass == WindowWidthSizeClass.MEDIUM

    val maxSectionInProduct = if (isTab) 6 else 4
    val maxColumnInProduct = if (isTab) 4 else 2

    Napier.d("Max $maxSectionInProduct Col $maxColumnInProduct")
    LaunchedEffect(Unit) {
        viewModel.onEvent(DiscoverUIEvent.OnLoadDiscovery(false))
    }
    Scaffold(topBar = {
        AppTopAppBar("Discover") {
            Icon(
                painter = painterResource(Res.drawable.logo_bml),
                modifier = Modifier.size(32.dp),
                contentDescription = null,
                tint = Color.White
            )
        }
    }) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            } else if (state.discoveries == null) {
                val infiniteTransition = rememberInfiniteTransition(label = "")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.5f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = androidx.compose.animation.core.tween(durationMillis = 1000),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp).alpha(alpha),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.logo_bml),
                        contentDescription = "No discovery found",
                        modifier = Modifier.size(120.dp)
                    )
                    Text(
                        "No discoveries found at the moment.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp, bottom = 16.dp)) {
                    item {
                        val offers = state.discoveries?.offers ?: emptyList()
                        if (offers.isNotEmpty()) {
                            OfferBannerCarousel(offers)
                            Spacer(modifier = Modifier.height(24.dp))
                        } else {
                            // Fallback if no offers are available
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                                    .height(180.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.logo_bml), // Replace with your banner image
                                    contentDescription = "Banner Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    item {
                        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            CategoryTitle("Services", "View All") {

                            }
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            items(state.discoveries!!.services) { service ->
                                ServiceItem(service)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        val product = state.discoveries!!.products.slice(
                            IntRange(
                                0,
                                maxSectionInProduct.minus(1)
                            )
                        )

                        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            CategoryTitle("Popular Products", "View All") {

                            }
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(maxColumnInProduct),
                                modifier = Modifier.fillMaxWidth()
                                    .heightIn(max = 800.dp),
                                userScrollEnabled = false,
                                contentPadding = PaddingValues(bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                this@LazyVerticalGrid.items(items = product) {
                                    ProductItem(it) {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun OfferBannerCarousel(offers: List<OfferModel>) {
    var currentOfferIndex by remember { mutableStateOf(0) }

    LaunchedEffect(offers) {
        if (offers.isNotEmpty()) {
            while (true) {
                delay(3000) // Change offer every 3 seconds
                currentOfferIndex = (currentOfferIndex + 1) % offers.size
            }
        }
    }

    AnimatedContent(targetState = currentOfferIndex, label = "offer_banner") { targetIndex ->
        if (offers.isNotEmpty()) {
            val offer = offers[targetIndex]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = offer.image,
                    contentDescription = offer.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(Res.drawable.logo_bml) // Optional placeholder
                )
            }
        }
    }
}

@Composable
fun CategoryTitle(title: String, viewAllText: String, onViewAllClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton(
            onClick = onViewAllClick
        ) {
            Text(
                text = viewAllText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ServiceItem(service: ServiceModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(service.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(Res.drawable.logo_bml),
                contentDescription = null,

                modifier = Modifier.size(75.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
                onLoading = {
                    Napier.e("imgload Loading")
                },
                onSuccess = {
                    Napier.e("imgload Success Loaded")
                },
                onError = {
                    Napier.e("imgload Error on Load ${it.result}")
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(service.name, fontSize = 14.sp, maxLines = 2, modifier = Modifier.width(60.dp))
    }
}

@Composable
fun ProductItem(product: ProductModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .widthIn(max = 160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(Res.drawable.logo_bml),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop,
                onLoading = {
                    Napier.e("imgload Loading")
                },
                onSuccess = {
                    Napier.e("imgload Success Loaded")
                },
                onError = {
                    Napier.e("imgload Error on Load ${it.result}")
                }
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "Rp ${product.price}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}