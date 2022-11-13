package com.dobashi.todolist_jetpack.other

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

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
    val deepLinks = listOf(
        navDeepLink {
            uriPattern = "todolist_jetpack://${route}/{${argumentName}}"
        })
}


object RegistrationDestination: Destination {
    override val route: String = "registration"
    const val argumentName = "createTime"
    val arguments = listOf(
        navArgument(argumentName) { type = NavType.StringType }
    )
    val routeWithArgs = "${route}/{${argumentName}}"
}
