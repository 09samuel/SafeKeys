package com.example.safekeys.ui.auth.login

sealed interface LoginEvent {
    data class SetPassword(val password: String) : LoginEvent

    data class Submit(val onClick: () -> Unit) : LoginEvent
}