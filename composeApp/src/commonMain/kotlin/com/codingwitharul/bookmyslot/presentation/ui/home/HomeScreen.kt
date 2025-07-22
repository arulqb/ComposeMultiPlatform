package com.codingwitharul.bookmyslot.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.codingwitharul.bookmyslot.presentation.ui.home.components.AppBottomBar
import com.codingwitharul.bookmyslot.presentation.ui.home.components.BottomBarScreen
import com.codingwitharul.bookmyslot.presentation.ui.home.components.bottomBarScreens
import com.codingwitharul.bookmyslot.presentation.ui.home.screens.HistoryScreen
import com.codingwitharul.bookmyslot.presentation.ui.home.screens.ProfileScreen
import com.codingwitharul.bookmyslot.presentation.ui.home.screens.booking.BookingScreen
import com.codingwitharul.bookmyslot.presentation.ui.home.screens.discovery.DiscoverScreen
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    var currentDestination by rememberSaveable { mutableStateOf(BottomBarScreen.Discover.route) }
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val useNavRail =
        adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM

    if (useNavRail) {
        Row(horizontalArrangement = Arrangement.Center) {
            NavigationRail(windowInsets = NavigationRailDefaults.windowInsets) {
                Spacer(Modifier.weight(1f))
                bottomBarScreens.forEach { item ->
                    NavigationRailItem(
                        selected = currentDestination == item.route,
                        onClick = {
                            currentDestination = item.route
                        },
                        icon = {
                            Image(vectorResource(item.icon), contentDescription = item.title)
                        },
                        label = { Text(item.title) }
                    )
                }
                Spacer(Modifier.weight(1f))
            }
            Column(modifier = Modifier.weight(1f)) {
                when (currentDestination) {
                    BottomBarScreen.Discover.route -> DiscoverScreen(adaptiveInfo.windowSizeClass.windowWidthSizeClass)
                    BottomBarScreen.History.route -> HistoryScreen()
                    BottomBarScreen.Book.route -> BookingScreen()
                    BottomBarScreen.Profile.route -> ProfileScreen()
                }
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                AppBottomBar(
                    currentDestination = currentDestination,
                    onItemSelected = { screen ->
                        currentDestination = screen.route
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        ) {
            Box(Modifier.padding(bottom = it.calculateBottomPadding())) {
                NavHost(
                    navController = navController,
                    startDestination = BottomBarScreen.Discover.route
                ) {
                    composable(BottomBarScreen.Discover.route) { DiscoverScreen(adaptiveInfo.windowSizeClass.windowWidthSizeClass) }
                    composable(BottomBarScreen.History.route) { HistoryScreen() }
                    composable(BottomBarScreen.Book.route) { BookingScreen() }
                    composable(BottomBarScreen.Profile.route) { ProfileScreen() }
                }
            }
        }
    }


}