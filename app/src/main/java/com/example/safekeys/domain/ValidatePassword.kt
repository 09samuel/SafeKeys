package com.example.safekeys.domain


class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if(password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password should consist at least 8 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() }
                && password.any { it.isUpperCase() }
        if(!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one uppercase letter and digit"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}