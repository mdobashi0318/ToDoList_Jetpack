package com.dobashi.todolist_jetpack.other

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}


object ListDestination: Destination {
    override val route: String = "list"

}

object DetailDestination: Destination {
    override val route: String = "details"
    const val argumentName = "createTime"
    val arguments = listOf(
        navArgument(argumentName) { type = NavType.StringType }
    )
    val routeWithArgs = "${route}/{${argumentName}}"
}