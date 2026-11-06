package com.rojas.consumodatos.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rojas.consumodatos.ui.contact.ContactScreen
import com.rojas.consumodatos.ui.task.TaskScreen

sealed class Screen(val route: String, val title: String) {
    object Contacts : Screen("contacts", "Contactos")
    object Tasks : Screen("tasks", "Tareas")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(Screen.Contacts, Screen.Tasks)

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        label = { Text(screen.title) },
                        icon = { Text(screen.title.take(1)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Contacts.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Contacts.route) {
                ContactScreen(viewModel = hiltViewModel())
            }
            composable(Screen.Tasks.route) {
                TaskScreen(viewModel = hiltViewModel())
            }
        }
    }
}
