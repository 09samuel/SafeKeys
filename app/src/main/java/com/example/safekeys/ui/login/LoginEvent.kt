package com.example.safekeys.ui.login

import com.example.safekeys.ui.auth.SignUpEvent

sealed interface LoginEvent {
    data class SetPassword(val password: String) : LoginEvent

    data class Submit(val onClick: () -> Unit) : LoginEvent
}