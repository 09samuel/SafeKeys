package com.example.safekeys.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object SignUp: Screen()
    @Serializable
    data object Login: Screen()
    @Serializable
    data object Home: Screen()
}