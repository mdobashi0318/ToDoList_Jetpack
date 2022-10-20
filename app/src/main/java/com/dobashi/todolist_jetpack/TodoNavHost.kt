package com.dobashi.todolist_jetpack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dobashi.todolist_jetpack.other.DetailDestination
import com.dobashi.todolist_jetpack.other.ListDestination
import kotlinx.coroutines.runBlocking

@Composable
fun TodoNavHost(modifier: Modifier = Modifier) {

    val todoModel = runBlocking {
        TodoApplication.database.todoDao().getAll()
    }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ListDestination.route
    ) {
        composable(ListDestination.route) {
            TodoListScreen(
                todoModel = todoModel,
                navController = navController
            )
        }

        composable(
            route = DetailDestination.routeWithArgs,
            arguments = DetailDestination.arguments
        ) { arg ->
            TodoDetailScreen(
                createTime = arg.arguments?.getString(DetailDestination.argumentName).toString(),
                navController = navController
            )
        }

    }
}

