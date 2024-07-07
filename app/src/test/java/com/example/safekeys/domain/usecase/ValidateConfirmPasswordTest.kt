package com.example.safekeys.domain.usecase

import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class ValidateConfirmPasswordTest{
    private lateinit var validateConfirmPassword: ValidateConfirmPassword

    @Before
    fun setUp(){
        validateConfirmPassword = ValidateConfirmPassword()
    }

    @Test
    fun `passwords match should return successful result`() {
        val password = "password123"
        val confirmPassword = "password123"

        val result = validateConfirmPassword.execute(password, confirmPassword)

        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorMessage).isNull()
    }

    @Test
    fun `passwords do not match should return unsuccessful result with error message`() {
        val password = "password123"
        val confirmPassword = "password321"

        val result = validateConfirmPassword.execute(password, confirmPassword)

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isEqualTo("The passwords don't match")
    }
}