package com.example.safekeys.domain

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)