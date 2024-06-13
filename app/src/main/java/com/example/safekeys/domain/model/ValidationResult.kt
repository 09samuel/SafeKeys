package com.example.safekeys.domain.model

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)