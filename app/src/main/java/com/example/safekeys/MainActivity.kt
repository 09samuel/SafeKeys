package com.example.safekeys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.safekeys.navigation.Screen
import com.example.safekeys.screens.CredentialsScreen
import com.example.safekeys.screens.LoginScreen
import com.example.safekeys.screens.SignUpScreen
import com.example.safekeys.ui.auth.SignUpViewModel
import com.example.safekeys.ui.home.CredentialViewModel
import com.example.safekeys.ui.login.LoginViewModel
import com.example.safekeys.ui.theme.SafeKeysTheme

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "safekeys.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val signUpViewModelFactory = SignUpViewModelFactory(db.userDao)
        val signUpViewModel =
            ViewModelProvider(this, signUpViewModelFactory)[SignUpViewModel::class.java]

        val credentialViewModelFactory = CredentialViewModelFactory(db.credentialDao)
        val credentialViewModel =
            ViewModelProvider(this, credentialViewModelFactory).get(CredentialViewModel::class.java)

        val loginViewModelFactory = LoginViewModelFactory(db.userDao)
        val loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

        signUpViewModel.checkRegistration()

        enableEdgeToEdge()
        setContent {
            SafeKeysTheme {
                val navController = rememberNavController()
                val credentialState by credentialViewModel.state.collectAsState()
                val signUpState by signUpViewModel.signUpState.collectAsState()
                val loginState by loginViewModel.loginState.collectAsState()

                NavHost(navController = navController, startDestination = if (signUpState.isRegistered) Screen.Login else Screen.SignUp) {
                    composable<Screen.SignUp> {
                        SignUpScreen(
                            viewmodel = signUpViewModel,
                            state = signUpState,
                            onEvent = signUpViewModel::onEvent,
                            onClick = {
                                navController.navigate(Screen.Home) {
                                    popUpTo(Screen.SignUp) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable<Screen.Login> {
                        LoginScreen(
                            viewmodel = loginViewModel,
                            state = loginState,
                            onEvent = loginViewModel::onEvent,
                            onClick = {
                                navController.navigate(Screen.Home) {
                                    popUpTo(Screen.Login) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable<Screen.Home> {
                        CredentialsScreen(
                            state = credentialState,
                            viewModel = credentialViewModel,
                            onEvent = credentialViewModel::onEvent,
                            onClick = {
                                if (navController.canGoBack) {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Extension property to check if navigation controller can go back
    private val NavHostController.canGoBack: Boolean
        get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
}

// Custom ViewModelFactory to handle ViewModel creation
class CredentialViewModelFactory(
    private val credentialDao: CredentialDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CredentialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CredentialViewModel(credentialDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SignUpViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoginViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
