package com.example.safekeys.ui.auth

data class SignUpState(
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isRegistered: Boolean = false
)
