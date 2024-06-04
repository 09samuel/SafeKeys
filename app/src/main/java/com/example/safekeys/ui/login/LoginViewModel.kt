package com.example.safekeys.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safekeys.User
import com.example.safekeys.UserDao
import com.example.safekeys.domain.ValidatePassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LoginViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SetPassword -> {
                _loginState.update { it.copy(password = event.password) }
            }

            is LoginEvent.Submit -> {
                login(loginState.value.password, event.onClick)
            }
        }
    }

    private fun login(password: String, onClick: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user: User? = userDao.getUser()
                if (user != null) {
                    val salt = user.salt
                    val pass = user.password

                    val hashedPassword = BCrypt.hashpw(password, salt)

                    if (hashedPassword == pass) {
                        Log.i("LoginViewModel", "Password is correct")
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
}
