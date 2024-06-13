package com.example.safekeys.domain.usecase

import com.example.safekeys.domain.model.ValidationResult

class ValidateConfirmPassword {
    fun execute(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}