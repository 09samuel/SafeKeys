package com.example.safekeys.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safekeys.User
import com.example.safekeys.UserDao
import com.example.safekeys.domain.ValidateConfirmPassword
import com.example.safekeys.domain.ValidatePassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class SignUpViewModel(
    private val userDao: UserDao,
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
) : ViewModel() {

//    private val _isRegistered = MutableStateFlow(false)
//    val isRegistered: StateFlow<Boolean> = _isRegistered

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun checkRegistration() {
        CoroutineScope(Dispatchers.IO).launch {
            val user: User? = userDao.getUser()
            if (user != null) {
                _signUpState.update { it.copy(isRegistered = true) }
            } else {
                _signUpState.update { it.copy(isRegistered = false) }
            }
        }
    }

    fun signUp(password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val salt = BCrypt.gensalt()
            val hashedPassword = BCrypt.hashpw(password, salt)

            val user = User(password = hashedPassword, salt = salt)

            userDao.insertUser(user)
            _signUpState.update { it.copy(isRegistered = true) }
            //_isAuthenticated.value = true
        }
    }

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SetConfirmPassword -> {
                _signUpState.update { it.copy(confirmPassword = event.confirmPassword) }
            }

            is SignUpEvent.SetPassword -> {
                _signUpState.update { it.copy(password = event.password) }
            }

            is SignUpEvent.Submit -> {
                submitData { event.onClick }
            }
        }
    }

    private fun submitData(onClick: () -> Unit) {
        val password = signUpState.value.password
        val confirmPassword = signUpState.value.confirmPassword

        val passwordResult = validatePassword.execute(password)
        val confirmPasswordResult = validateRepeatedPassword.execute(
            password, confirmPassword
        )

//        val hasError = listOf(
//            passwordResult,
//            confirmPasswordResult,
//        ).any { !it.successful }

        if (!passwordResult.successful) {
            _signUpState.update { it.copy(passwordError = passwordResult.errorMessage) }
            return
        }

        if (!confirmPasswordResult.successful) {
            _signUpState.update { it.copy(confirmPasswordError = confirmPasswordResult.errorMessage) }
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val salt = BCrypt.gensalt()
            val hashedPassword = BCrypt.hashpw(password, salt)

            val user = User(password = hashedPassword, salt = salt)

            userDao.insertUser(user)
            _signUpState.update { it.copy(isRegistered = true) }
            //_isAuthenticated.value = true
            viewModelScope.launch {
                onClick()
            }
        }
//        viewModelScope.launch {
//            validationEventChannel.send(ValidationEvent.Success)
//        }
    }

//    sealed class ValidationEvent {
//        object Success: ValidationEvent()
//    }

}