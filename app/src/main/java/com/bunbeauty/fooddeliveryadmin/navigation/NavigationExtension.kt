package com.bunbeauty.fooddeliveryadmin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafe(directions: NavDirections) {
    val action = currentDestination?.getAction(directions.actionId)
    if (action != null) {
        navigate(directions)
    }
}

fun NavController.navigateSafe(directionsId: Int) {
    val action = currentDestination?.getAction(directionsId)
    if (action != null) {
        navigate(directionsId)
    }
}
