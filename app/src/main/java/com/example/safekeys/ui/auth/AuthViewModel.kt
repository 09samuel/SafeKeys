package com.example.safekeys.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safekeys.data.model.User
import com.example.safekeys.domain.repository.UserRepository
import com.example.safekeys.domain.usecase.ValidateConfirmPassword
import com.example.safekeys.domain.usecase.ValidatePassword
import com.example.safekeys.ui.auth.autofill.AutofillAuthEvent
import com.example.safekeys.ui.auth.autofill.AutofillAuthState
import com.example.safekeys.ui.auth.login.LoginEvent
import com.example.safekeys.ui.auth.login.LoginState
import com.example.safekeys.ui.auth.signup.SignUpEvent
import com.example.safekeys.ui.auth.signup.SignUpState
import com.example.safekeys.utils.NotificationHelper
import com.example.safekeys.utils.SharedPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _autofillAuthState = MutableStateFlow(AutofillAuthState())
    val autofillAuthState: StateFlow<AutofillAuthState> = _autofillAuthState

//    init {
//        checkRegistration()
//    }

    fun checkRegistration(): Boolean {
        return sharedPreferenceHelper.isUserRegistered()
    }

    fun setRegistration() {
        sharedPreferenceHelper.setUserRegistered()
    }


    fun setLogin() {
        userRepository.setLogin()
    }

    fun checkLogin(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    fun setLogOut() {
        return userRepository.setLogOut()
    }


    fun onSignUpEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SetConfirmPassword -> {
                _signUpState.update { it.copy(confirmPassword = event.confirmPassword) }
            }

            is SignUpEvent.SetPassword -> {
                _signUpState.update { it.copy(password = event.password) }
            }

            is SignUpEvent.Submit -> {
                signUp(event.onClick)
            }
        }
    }

    private fun signUp(onClick: () -> Unit) {
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

            try {
                userRepository.insertUser(user)

                setRegistration()

                viewModelScope.launch {
                    onClick()
                }
            } catch (e: Exception) {
                Log.i("registration", "reg fail")
            }
        }
//        viewModelScope.launch {
//            validationEventChannel.send(ValidationEvent.Success)
//        }
    }

    fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SetPassword -> {
                _loginState.update { it.copy(password = event.password) }
            }

            is LoginEvent.Submit -> {
                login(loginState.value.password, event.onClick)
            }
        }
    }

    fun onAutofillAuthEvent(event: AutofillAuthEvent) {
        when (event) {
            is AutofillAuthEvent.SetPassword -> {
                _autofillAuthState.update { it.copy(password = event.password) }
            }

            is AutofillAuthEvent.Submit -> {
                autofillLogin(autofillAuthState.value.password, event.onClick)
            }
        }
    }

    private fun login(password: String, onClick: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user: User? = userRepository.getUser()
                if (user != null) {
                    val salt = user.salt
                    val pass = user.password

                    val hashedPassword = BCrypt.hashpw(password, salt)

                    if (hashedPassword == pass) {
                        setLogin()
                        notificationHelper.showNotification()
                        viewModelScope.launch {
                            onClick()
                        }
                    } else {
                        Log.i("LoginViewModel", "Incorrect password")
                        _loginState.update { it.copy(passwordError = "Incorrect password") }
                    }
                } else {
                    Log.i("LoginViewModel", "User not found")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error logging in", e)
            }
        }
    }

    private fun autofillLogin(password: String, onClick: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user: User? = userRepository.getUser()
                if (user != null) {
                    val salt = user.salt
                    val pass = user.password

                    val hashedPassword = BCrypt.hashpw(password, salt)

                    if (hashedPassword == pass) {
                        //saveToken(context)
                        Log.i("LoginViewModel", "Password is correct")
                        viewModelScope.launch {
                            onClick()
                        }
                    } else {
                        Log.i("LoginViewModel", "Incorrect password")
                        _autofillAuthState.update { it.copy(passwordError = "Incorrect password") }
                    }
                } else {
                    Log.i("LoginViewModel", "User not found")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error logging in", e)
            }
        }
    }

}
