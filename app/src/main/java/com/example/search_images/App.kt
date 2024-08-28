package com.example.search_images

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.search_images.const.AppScreen
import com.example.search_images.ui.bookmark.BookmarkScreen
import com.example.search_images.ui.detail.ImageDetailScreen
import com.example.search_images.ui.search.SearchScreen

@Composable
fun App(
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val navController = rememberNavController()
    val screens = listOf(
        AppScreen.Search.route,
        AppScreen.Bookmark.route
    )
    val icons = mapOf(
        AppScreen.Search.route to R.drawable.ic_search,
        AppScreen.Bookmark.route to R.drawable.ic_favorite
    )
    val snackBarHostState = remember { SnackbarHostState() }

    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactScreen(
                navController = navController,
                snackBarHostState = snackBarHostState,
                screens = screens,
                icons = icons
            )
        }
        else -> {
            ExpandedScreen(
                navController = navController,
                snackBarHostState = snackBarHostState,
                screens = screens,
                icons = icons
            )
        }
    }

}

@Composable
private fun CompactScreen(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    screens: List<String>,
    icons: Map<String, Int>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            BottomAppBar {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            icons[screen]?.let { painterResource(id = it) }?.let {
                                Icon(painter = it, contentDescription = null)
                            }
                        },
                        label = { Text(text = screen) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen } == true,
                        onClick = {
                            navController.navigate(screen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        val animMilli = 300

        NavHost(
            navController = navController,
            startDestination = AppScreen.Search.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(animMilli)) },
            exitTransition = { fadeOut(tween(animMilli)) }
        ) {
            composable(AppScreen.Search.route) {
                SearchScreen(
                    snackBarHostState = snackBarHostState,
                    goToDetailScreen = { imageUrl ->
                        navController.navigate("${AppScreen.ImageDetail.route}/$imageUrl")
                    }
                )
            }
            composable(AppScreen.Bookmark.route) {
                BookmarkScreen(
                    snackBarHostState = snackBarHostState,
                    goToDetailScreen = { imageUrl ->
                        navController.navigate("${AppScreen.ImageDetail.route}/$imageUrl")
                    }
                )
            }
            dialog(
                route = "${AppScreen.ImageDetail.route}/{imageUrl}",
                arguments = listOf(navArgument("imageUrl") { type = NavType.StringType }),
                dialogProperties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
            ) {
                ImageDetailScreen(
                    backToList = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun ExpandedScreen(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    screens: List<String>,
    icons: Map<String, Int>
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValue ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            NavigationRail {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationRailItem(
                        icon = {
                            icons[screen]?.let { painterResource(id = it) }?.let {
                                Icon(painter = it, contentDescription = null)
                            }
                        },
                        label = {
                            Text(text = screen)
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen } == true,
                        onClick = {
                            navController.navigate(screen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
            NavHost(
                navController = navController,
                startDestination = AppScreen.Search.route
            ) {
                composable(AppScreen.Search.route) {
                    SearchScreen(
                        snackBarHostState = snackBarHostState,
                        goToDetailScreen = { imageUrl ->
                            navController.navigate("${AppScreen.ImageDetail.route}/$imageUrl")
                        }
                    )
                }
                composable(AppScreen.Bookmark.route) {
                    BookmarkScreen(
                        snackBarHostState = snackBarHostState,
                        goToDetailScreen = { imageUrl ->
                            navController.navigate("${AppScreen.ImageDetail.route}/$imageUrl")
                        }
                    )
                }
                composable(
                    route = "${AppScreen.ImageDetail.route}/{imageUrl}",
                    arguments = listOf(navArgument("imageUrl") { type = NavType.StringType })
                ) {
                    ImageDetailScreen(
                        backToList = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
