package com.example.lezhintest.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lezhintest.R
import com.example.lezhintest.const.LezhinScreen
import com.example.lezhintest.ui.bookmark.BookmarkScreen
import com.example.lezhintest.ui.search.SearchScreen

@Composable
fun MainScreen(
    windowWidthSizeClass: WindowWidthSizeClass
) {
    val navController = rememberNavController()
    val icons = mapOf(
        LezhinScreen.Search.route to R.drawable.ic_search,
        LezhinScreen.Bookmark.route to R.drawable.ic_favorite
    )

    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactScreen(
                navController = navController,
                icons = icons
            )
        }
        else -> {
            ExpandedScreen(
                navController = navController,
                icons = icons
            )
        }
    }

}

@Composable
private fun CompactScreen(
    navController: NavHostController,
    icons: Map<String, Int>
) {
    val items = listOf(
        LezhinScreen.Search,
        LezhinScreen.Bookmark
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            BottomNavigation {
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            icons[screen.route]?.let { painterResource(id = it) }?.let {
                                Icon(painter = it, contentDescription = null)
                            }
                        },
                        label = {
                            Text(
                                text = stringResource(id = screen.resourceId)
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LezhinScreen.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(LezhinScreen.Search.route) {
                SearchScreen(navController = navController)
            }
            composable(LezhinScreen.Bookmark.route) {
                BookmarkScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun ExpandedScreen(
    navController: NavHostController,
    icons: Map<String, Int>
) {
    val items = listOf(
        LezhinScreen.Search,
        LezhinScreen.Bookmark
    )

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { screen ->
                NavigationRailItem(
                    icon = {
                        icons[screen.route]?.let { painterResource(id = it) }?.let {
                            Icon(painter = it, contentDescription = null)
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(id = screen.resourceId)
                        )
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
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
            startDestination = LezhinScreen.Search.route
        ) {
            composable(LezhinScreen.Search.route) {
                SearchScreen(navController = navController)
            }
            composable(LezhinScreen.Bookmark.route) {
                BookmarkScreen(navController = navController)
            }
        }
    }
}
