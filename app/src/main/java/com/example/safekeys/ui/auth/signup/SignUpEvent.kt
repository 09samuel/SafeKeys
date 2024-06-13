package com.example.safekeys.ui.auth.signup

sealed interface SignUpEvent {
    data class SetPassword(val password: String) : SignUpEvent
    data class SetConfirmPassword(val confirmPassword: String) : SignUpEvent

    data class Submit(val onClick: () -> Unit) : SignUpEvent
}