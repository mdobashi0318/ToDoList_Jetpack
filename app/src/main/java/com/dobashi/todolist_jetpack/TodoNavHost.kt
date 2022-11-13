package com.dobashi.todolist_jetpack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dobashi.todolist_jetpack.other.DetailDestination
import com.dobashi.todolist_jetpack.other.ListDestination
import com.dobashi.todolist_jetpack.other.RegistrationDestination

@Composable
fun TodoNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ListDestination.route
    ) {
        composable(ListDestination.route) {
            TodoListScreen(
                navController = navController
            )
        }

        composable(
            route = DetailDestination.routeWithArgs,
            arguments = DetailDestination.arguments,
            deepLinks = DetailDestination.deepLinks
        ) { arg ->
            TodoDetailScreen(
                createTime = arg.arguments?.getString(DetailDestination.argumentName).toString(),
                navController = navController
            )
        }


        composable(
            route = RegistrationDestination.route
        ) { _ ->
            TodoRegistrationScreen(navController = navController)
        }

        composable(
            route = RegistrationDestination.routeWithArgs,
            arguments = RegistrationDestination.arguments
        ) { arg ->
            TodoRegistrationScreen(
                createTime = arg.arguments?.getString(RegistrationDestination.argumentName),
                navController = navController
            )
        }

    }
}

