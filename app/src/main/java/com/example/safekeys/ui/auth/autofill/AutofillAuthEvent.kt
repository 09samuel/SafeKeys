package com.example.safekeys.ui.auth.autofill

sealed interface AutofillAuthEvent {
        data class SetPassword(val password: String) : AutofillAuthEvent
        data class Submit(val onClick: () -> Unit) : AutofillAuthEvent
}